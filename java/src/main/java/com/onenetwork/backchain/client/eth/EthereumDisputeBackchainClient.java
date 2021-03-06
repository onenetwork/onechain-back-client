/**
 */
package com.onenetwork.backchain.client.eth;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
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
    EthereumHelper.await(
      () -> disputeBackchainABI.submitDispute(
        EthereumHelper.hashStringToBytes(dispute.getDisputeID()),
        new Address(dispute.getDisputingParty()),
        EthereumHelper.hashStringToBytes(dispute.getDisputedTransactionID()),
        EthereumHelper.convertAndGetBytes32DA(dispute.getDisputedBusinessTransactionIDs()),
        new Utf8String(dispute.getReason().toString())).get());
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
    return EthereumHelper.await(() -> getDispute(EthereumHelper.hashStringToBytes(disputeID), disputeID));
  }

  @Override
  public int getDisputeCount(DisputeFilter disputeFilter) {
    return getFilterDisputeIDs(disputeFilter).size();
  }

  private List<Bytes32> getFilterDisputeIDs(DisputeFilter disputeFilter) {
    DynamicArray<Bytes32> disputeIDHash = EthereumHelper.await(
      () -> disputeBackchainABI.filterDisputeByHeaders(
        EthereumHelper.convertAndGetBytes32DA(disputeFilter.getDisputeIDs()),
        EthereumHelper.convertAndGetAddressDA(disputeFilter.getDisputingParties()),
        EthereumHelper.convertAndGetBytes32DA(disputeFilter.getDisputedTransactionIDs()),
        EthereumHelper.convertAndGetBytes32DA(disputeFilter.getDisputedBusinessTransactionIDs())).get());

    if (disputeIDHash.getValue().size() <= 0) {
      return disputeIDHash.getValue();
    }
    DynamicArray<Bytes32> disputeIDHashDetail = EthereumHelper.await(
      () -> disputeBackchainABI.filterDisputeByDetail(
        disputeIDHash,
        EthereumHelper.getTimeInUint256(disputeFilter.getSubmittedStartDate()),
        EthereumHelper.getTimeInUint256(disputeFilter.getSubmittedEndDate()),
        EthereumHelper.getTimeInUint256(disputeFilter.getCloseStartDate()),
        EthereumHelper.getTimeInUint256(disputeFilter.getCloseEndDate()),
        EthereumHelper.convertAndGetUint256DA(disputeFilter.getStates()),
        EthereumHelper.convertAndGetUint256DA(disputeFilter.getReasons())).get());

    return disputeIDHashDetail.getValue();
  }

  private Dispute getDispute(Bytes32 disputeIDHash, String disputeID) throws DecoderException {
    Dispute dispute = new Dispute().setDisputeID(disputeID);
    @SuppressWarnings("rawtypes")
    List<Type> headerData = EthereumHelper.await(() -> disputeBackchainABI.getDisputeHeader(disputeIDHash).get());
    @SuppressWarnings("rawtypes")
    List<Type> detailData = EthereumHelper.await(() -> disputeBackchainABI.getDisputeDetail(disputeIDHash).get());
    dispute.setDisputingParty(headerData.get(0).toString())
      .setDisputedTransactionID(EthereumHelper.hashBytesToString((Bytes32) headerData.get(1)));
    List<?> businessTransactionList = (List<?>) headerData.get(2).getValue();
    String[] businessTrasactionIDs = new String[businessTransactionList.size()];
    for (int i = 0; i < businessTrasactionIDs.length; i++) {
      businessTrasactionIDs[i] = EthereumHelper.hashBytesToString((Bytes32) businessTransactionList.get(i));
    }
    dispute.setDisputedBusinessTransactionIDs(businessTrasactionIDs);
    Calendar submitDateCal = Calendar.getInstance();
    submitDateCal.setTimeInMillis(((BigInteger) (detailData.get(0).getValue())).longValue());
    dispute.setSubmittedDate(submitDateCal);
    long closeTime = ((BigInteger) (detailData.get(1).getValue())).longValue();
    if (closeTime > 0L) {
      Calendar closedDateCal = Calendar.getInstance();
      closedDateCal.setTimeInMillis(closeTime);
      dispute.setCloseDate(closedDateCal);
    }
    dispute.setState(Dispute.State.valueOf((String) detailData.get(2).getValue()));
    dispute.setReason(Dispute.Reason.valueOf((String) detailData.get(3).getValue()));
    return dispute;
  }

  @Override
  public List<Dispute> filterDisputes(DisputeFilter disputeFilter) {
    List<Bytes32> disputeIDHashList = getFilterDisputeIDs(disputeFilter);
    if (disputeIDHashList.size() <= 0) {
      return new ArrayList<>();
    }

    return EthereumHelper.await(() -> {
      int disputeCount = disputeIDHashList.size();
      List<Dispute> disputes = new ArrayList<>(disputeCount);
      for (int i = 0; i < disputeCount; i++) {
        disputes.add(getDispute(disputeIDHashList.get(i), EthereumHelper.hashBytesToString(disputeIDHashList.get(i))));
      }
      return disputes;
    });
  }

  @Override
  public int getDisputeSubmissionWindowInMinutes() {
    return EthereumHelper
      .await(() -> disputeBackchainABI.getDisputeSubmissionWindowInMinutes().get().getValue().intValue());
  }

  @Override
  public void setDisputeSubmissionWindowInMinutes(int timeInMinutes) {
    EthereumHelper
      .await(() -> disputeBackchainABI.setDisputeSubmissionWindowInMinutes(new Uint256(timeInMinutes)).get());
  }

  @Override
  public String getOrchestrator() {
    return EthereumHelper.await(() -> disputeBackchainABI.getOrchestrator().get().toString());
  }
}
