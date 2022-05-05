/**
 */
package com.onenetwork.backchain.client.eth;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.ClientTransactionManager;

import com.onenetwork.backchain.client.BackchainClientConfig;
import com.onenetwork.backchain.client.Dispute;
import com.onenetwork.backchain.client.Dispute.DisputeFilter;
import com.onenetwork.backchain.client.DisputeBackchainClient;

/**
 * Implementation of {@link DisputeBackchainClient} for an
 * <a href="https://www.ethereum.org/">Ethereum</a>-based Backchain.
 */
public class EthereumDisputeBackchainClient implements DisputeBackchainClient {

  private Web3j web3j;
  private DisputeBackchainABI disputeBackchainABI;
  private Credentials credentials;

  /**
   * @param config Ethereum-based {@link BackchainClientConfig}
   */
  public EthereumDisputeBackchainClient(EthereumConfig config) {
    web3j = Web3j.build(new HttpService(config.getUrl()));
    if (config.getPrivateKey() != null) {
      credentials = Credentials.create(config.getPrivateKey());
      disputeBackchainABI = DisputeBackchainABI.load(
        config.getDisputeBackchainContractAddress(),
        web3j,
        credentials,
        config.getGasPrice(),
        config.getGasLimit());
    }
    else {
      ClientTransactionManager tm = new ClientTransactionManager(web3j, "0x00000000000000000000000000000000");
      disputeBackchainABI = DisputeBackchainABI
        .load(config.getDisputeBackchainContractAddress(), web3j, tm, config.getGasPrice(), config.getGasLimit());
    }
  }

  @Override
  public void submitDispute(Dispute dispute) {
    if (EthereumHelper.isNullOrEmpty(dispute.getDisputeID())) {
      dispute.setDisputeID(EthereumHelper.await(() -> EthereumHelper.newHash()));
    }
    if (dispute.getDisputedBusinessTransactionIDs() == null) {
      dispute.setDisputedBusinessTransactionIDs(new String[0]);
    }
    if (EthereumHelper.isNullOrEmpty(dispute.getDisputingParty())) {
      dispute.setDisputingParty(credentials.getAddress());
    }
    else if (dispute.getDisputingParty().equals(credentials.getAddress())) {
      throw new IllegalArgumentException("Cannot submit dispute, as your address does not match the Disputing Party address");
    }
    if (EthereumHelper.isNullOrEmpty(dispute.getDisputedTransactionID())) {
      throw new IllegalArgumentException("DisputedTransactionId is a required field");
    }
    if (EthereumHelper.isNullOrEmpty(dispute.getReason())) {
      throw new IllegalArgumentException("Reason is a required field");
    }
    try {
      disputeBackchainABI.submitDispute(
          EthereumHelper.hashStringToBytes(dispute.getDisputeID()),
          dispute.getDisputingParty(),
          EthereumHelper.hashStringToBytes(dispute.getDisputedTransactionID()),
          EthereumHelper.convertAndGetBytes(dispute.getDisputedBusinessTransactionIDs()),
          dispute.getReason().toString()).send();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void closeDispute(String disputeID) {
    if (EthereumHelper.isNullOrEmpty(disputeID)) {
      new IllegalArgumentException("disputeID is required. It cannot be null or empty");
    }
    EthereumHelper.await(() -> disputeBackchainABI.closeDispute(EthereumHelper.hashStringToBytes(disputeID)));
  }

  @Override
  public Dispute getDispute(String disputeID) {
    if (EthereumHelper.isNullOrEmpty(disputeID)) {
      new IllegalArgumentException("disputeID is required. It cannot be null or empty");
    }
    return getDispute(EthereumHelper.hashStringToBytes(disputeID), disputeID);
  }

  @Override
  public int getDisputeCount(DisputeFilter disputeFilter) {
    return getFilterDisputeIDs(disputeFilter).size();
  }

  @SuppressWarnings("unchecked")
  private List<byte[]> getFilterDisputeIDs(DisputeFilter disputeFilter) {
    try {
      List<byte[]> disputeIDHash = disputeBackchainABI.filterDisputeByHeaders(
          EthereumHelper.convertAndGetBytes(disputeFilter.getDisputeIDs()),
          Arrays.asList(disputeFilter.getDisputingParties()),
          EthereumHelper.convertAndGetBytes(disputeFilter.getDisputedTransactionIDs()),
          EthereumHelper.convertAndGetBytes(disputeFilter.getDisputedBusinessTransactionIDs())).send();
  
      if (disputeIDHash.size() <= 0) {
        return disputeIDHash;
      }
      
      List<byte[]> disputeIDHashDetail = disputeBackchainABI.filterDisputeByDetail(
          disputeIDHash,
          EthereumHelper.getTimeInBigInteger(disputeFilter.getSubmittedStartDate()),
          EthereumHelper.getTimeInBigInteger(disputeFilter.getSubmittedEndDate()),
          EthereumHelper.getTimeInBigInteger(disputeFilter.getCloseStartDate()),
          EthereumHelper.getTimeInBigInteger(disputeFilter.getCloseEndDate()),
          EthereumHelper.convertAndGetBigIntegers(disputeFilter.getStates()),
          EthereumHelper.convertAndGetBigIntegers(disputeFilter.getReasons())).send();
  
      return disputeIDHashDetail;
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Dispute getDispute(byte[] disputeIDHash, String disputeID) {
    try {
      Dispute dispute = new Dispute().setDisputeID(disputeID);
      @SuppressWarnings("rawtypes")
      Tuple3<String, byte[], List<byte[]>> headerData = disputeBackchainABI.getDisputeHeader(disputeIDHash).send();
      @SuppressWarnings("rawtypes")
      Tuple4<BigInteger, BigInteger, String, String> detailData = disputeBackchainABI.getDisputeDetail(disputeIDHash).send();
      dispute.setDisputingParty(headerData.getValue1().toString())
        .setDisputedTransactionID(EthereumHelper.hashBytesToString(headerData.getValue2()));
      List<?> businessTransactionList = (List<?>) headerData.getValue3();
      String[] businessTrasactionIDs = new String[businessTransactionList.size()];
      for (int i = 0; i < businessTrasactionIDs.length; i++) {
        businessTrasactionIDs[i] = EthereumHelper.hashBytesToString((Bytes32) businessTransactionList.get(i));
      }
      dispute.setDisputedBusinessTransactionIDs(businessTrasactionIDs);
      Calendar submitDateCal = Calendar.getInstance();
      submitDateCal.setTimeInMillis(detailData.getValue1().longValue());
      dispute.setSubmittedDate(submitDateCal);
      long closeTime = detailData.getValue2().longValue();
      if (closeTime > 0L) {
        Calendar closedDateCal = Calendar.getInstance();
        closedDateCal.setTimeInMillis(closeTime);
        dispute.setCloseDate(closedDateCal);
      }
      dispute.setState(Dispute.State.valueOf(detailData.getValue3()));
      dispute.setReason(Dispute.Reason.valueOf(detailData.getValue4()));
      return dispute;
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Dispute> filterDisputes(DisputeFilter disputeFilter) {
    List<byte[]> disputeIDHashList = getFilterDisputeIDs(disputeFilter);
    if (disputeIDHashList.size() <= 0) {
      return new ArrayList<>();
    }

    int disputeCount = disputeIDHashList.size();
    List<Dispute> disputes = new ArrayList<>(disputeCount);
    for (int i = 0; i < disputeCount; i++) {
      disputes.add(getDispute(disputeIDHashList.get(i), EthereumHelper.hashBytesToString(disputeIDHashList.get(i))));
    }
    return disputes;
  }

  @Override
  public int getDisputeSubmissionWindowInMinutes() {
    try {
      return disputeBackchainABI.getDisputeSubmissionWindowInMinutes().send().intValue();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void setDisputeSubmissionWindowInMinutes(int timeInMinutes) {
    try {
      disputeBackchainABI.setDisputeSubmissionWindowInMinutes(new BigInteger("" + timeInMinutes)).send();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getOrchestrator() {
    try {
      return disputeBackchainABI.getOrchestrator().send();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
