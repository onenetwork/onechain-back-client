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
    String disputeTransactionID = EthereumHelper.newHash();
    Dispute dispute = new Dispute().setDisputedTransactionID(disputeTransactionID)
      .setReason(Reason.HASH_NOT_FOUND);
    try {
      dbc.submitDispute(dispute);
    }
    catch(Exception e) {
      e.printStackTrace();
      fail("subbmition Failed" + e.getMessage());
    }
    DisputeFilter disputeFilter = new DisputeFilter();
    Dispute[] resultDisputes = dbc.filterDisputes(disputeFilter);
    assertTrue(resultDisputes.length == 1);
    assertEquals(disputeTransactionID, resultDisputes[0].getDisputedTransactionID());
    assertEquals(Reason.HASH_NOT_FOUND, resultDisputes[0].getReason());
    try {
      dbc.closeDispute(resultDisputes[0].getDisputeID());
    }
    catch (Exception e) {
      fail("close Failed" + e.getMessage());
    }
    try {
      dbc.closeDispute(resultDisputes[0].getDisputeID());
      fail("second time close Failed");
    }
    catch (Exception e) {
    }
    
    Dispute resultDispute= dbc.getDispute(resultDisputes[0].getDisputeID());
    assertEquals(State.CLOSED, resultDispute.getState());
    assertEquals(disputeTransactionID, resultDispute.getDisputedTransactionID());
    assertEquals(Reason.HASH_NOT_FOUND, resultDispute.getReason());
  }
  
  private static EthereumConfig geEthereumConfig() {
    return new EthereumConfig().setUrl("http://localhost:8545")
      .setContentBackchainContractAddress("0xc5d4b021858a17828532e484b915149af5e1b138")
      .setDisputeBackchainContractAddress("0x4a6886a515a4b800f4591a6d6a60e6004a3645ab")
      .setPrivateKey("0x8ad0132f808d0830c533d7673cd689b7fde2d349ff0610e5c04ceb9d6efb4eb1")
      .setGasPrice(BigInteger.valueOf(1L)).setGasLimit(BigInteger.valueOf(999999999L));
  }

}
