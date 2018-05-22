package com.onenetwork.backchain.client.eth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigInteger;

import org.junit.Test;

import com.onenetwork.backchain.client.BackchainClientFactory;
import com.onenetwork.backchain.client.Dispute;
import com.onenetwork.backchain.client.Dispute.DisputeFilter;
import com.onenetwork.backchain.client.Dispute.Reason;
import com.onenetwork.backchain.client.Dispute.State;
import com.onenetwork.backchain.client.DisputeBackchainClient;

public class EthereumTestDBC {

  public EthereumTestDBC() {
  }

  @Test
  public void testOrchestrator() throws Exception {
    DisputeBackchainClient dbc = BackchainClientFactory.newDisputeBackchainClient(geEthereumConfig());
    assertEquals("0xece1355c30af00ff4f03f0e37f7822ce4b660aa3", dbc.getOrchestrator());
  }
  
  @Test
  public void testSubmitAndCloseDispute() throws Exception {
    String disputeTransactionID ;
    try {
     disputeTransactionID = EthereumHelper.newHash();
    }catch(Exception e) {}
    
    disputeTransactionID = EthereumHelper.newHash();
    String disputeID = EthereumHelper.newHash();
    Dispute dispute = new Dispute().setDisputeID(disputeID)
      .setDisputedTransactionID(disputeTransactionID).setReason(Reason.HASH_NOT_FOUND);
    submitDispute(dispute);
    closeDispute(dispute);
  }

  @Test
  public void testFindDispute() throws Exception {
    String disputeTransactionID = EthereumHelper.newHash();
    String disputeID = EthereumHelper.newHash();
    DisputeBackchainClient dbc = BackchainClientFactory.newDisputeBackchainClient(geEthereumConfig());
    DisputeFilter disputeFilter = new DisputeFilter().setReasons(new Reason[] { Reason.HASH_NOT_FOUND });
    Dispute[] resultDisputes = dbc.filterDisputes(disputeFilter);
    if (resultDisputes.length > 0) {
      for (Dispute dispute : resultDisputes) {
        assertEquals(Reason.HASH_NOT_FOUND, dispute.getReason());
      }
    }
    else {
      submitDispute(
        new Dispute().setDisputeID(disputeID).setDisputedTransactionID(disputeTransactionID)
          .setReason(Reason.HASH_NOT_FOUND));
      resultDisputes = dbc.filterDisputes(disputeFilter);
      if (resultDisputes.length > 0) {
        for (Dispute dispute : resultDisputes) {
          assertEquals(Reason.HASH_NOT_FOUND, dispute.getReason());
        }
      }
    }
  }

  private Dispute submitDispute(Dispute dispute) {
    DisputeBackchainClient dbc = BackchainClientFactory.newDisputeBackchainClient(geEthereumConfig());
    try {
      dbc.submitDispute(dispute);
    }
    catch (Exception e) {
      e.printStackTrace();
      fail("submission Failed" + e.getMessage());
    }

    Dispute dbcDispute = dbc.getDispute(dispute.getDisputeID());
    assertEquals(dispute.getDisputeID(), dbcDispute.getDisputeID());
    assertEquals(dispute.getReason(), dbcDispute.getReason());
    assertEquals(dispute.getDisputedTransactionID(), dbcDispute.getDisputedTransactionID());
    assertEquals(State.OPEN, dbcDispute.getState());
    assertTrue(dbcDispute.getCloseDate() == null);
    return dbcDispute;
  }

  private Dispute closeDispute(Dispute dispute) {
    DisputeBackchainClient dbc = BackchainClientFactory.newDisputeBackchainClient(geEthereumConfig());
    try {
      dbc.closeDispute(dispute.getDisputeID());
    }
    catch (Exception e) {
      e.printStackTrace();
      fail("submission Failed" + e.getMessage());
    }

    Dispute dbcDispute = dbc.getDispute(dispute.getDisputeID());
    assertEquals(dispute.getDisputeID(), dbcDispute.getDisputeID());
    assertEquals(dispute.getReason(), dbcDispute.getReason());
    assertEquals(dispute.getDisputedTransactionID(), dbcDispute.getDisputedTransactionID());
    assertEquals(State.CLOSED, dbcDispute.getState());
    assertTrue(dbcDispute.getCloseDate() != null && dbcDispute.getCloseDate().getTimeInMillis() >= dbcDispute.getSubmittedDate().getTimeInMillis());
    return dbcDispute;
  }

  private static EthereumConfig geEthereumConfig() {
    return new EthereumConfig().setUrl("http://192.168.201.55:8545")
      .setContentBackchainContractAddress("0xc5d4b021858a17828532e484b915149af5e1b138")
      .setDisputeBackchainContractAddress("0x4a6886a515a4b800f4591a6d6a60e6004a3645ab")
      .setPrivateKey("0x8ad0132f808d0830c533d7673cd689b7fde2d349ff0610e5c04ceb9d6efb4eb1")
      .setGasPrice(BigInteger.valueOf(0L)).setGasLimit(BigInteger.valueOf(999999l));
  }

}
