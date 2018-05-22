package com.onenetwork.backchain.client;

import java.util.List;

/**
 * Interface for interacting with the DisputeBackchain.  Acquire an instance
 * via {@link BackchainClientFactory}.  
 */
public interface DisputeBackchainClient {

  /**
   * Submit a new {@link Dispute} to the DisputeBackchain.
   * 
   * @param dispute a new {@link Dispute}
   */
  void submitDispute(Dispute dispute);
  
  /**
   * Close the {@link Dispute} with matching disputeID.  
   * 
   * <p/>
   * WARNING - only the disputingParty on a Dispute can Close it.  
   * 
   * @param disputeID
   */
  void closeDispute(String disputeID);
  
  /**
   * Disputes are stored in the DisputeBackchain. 
   * This method returns the {@link Dispute} stored matching with the disputeID.
   * 
   * @param disputeID of the desired {@link Dispute}
   * @return {@link Dispute} 
   */ 
  Dispute getDispute(String disputeID);
  
  /**
   * Returns the address of the entity acting as the Orchestrator for the DisputeBackchain.
   * 
   * @return address of the entity acting as the Orchestrator for the DisputeBackchain
   */
  String getOrchestrator();
  
  /**
   * Get the Dispute submission window.  Disputes may only be filed within this amount of time
   * from the point the transaction in question's block was added to the blockchain.   
   * 
   * @return Dispute submission window, in minutes
   */
  int getDisputeSubmissionWindowInMinutes();
  
  /**
   * Set the Dispute submission window.Disputes may only be filed within this amount of time
   * from the point the transaction in question's block was added to the blockchain.   
   * 
   * <p/>
   * WARNING - only the Orchestrator is permitted to change the dispute submission window
   * 
   * @param timeInMinutes 
   */
  void setDisputeSubmissionWindowInMinutes(int timeInMinutes);
  
  /**
   * Returns all Disputes matching the given filter.
   * 
   * @param disputeFilter filter criteria 
   * @return matching Disputes
   */
  List<Dispute> filterDisputes(Dispute.DisputeFilter disputeFilter);
  
  /**
   * Returns the number of Disputes matching the given filter.
   *  
   * @param disputeFilter
   * @param disputeFilter filter criteria 
   * @return number of matching Disputess
   */
  int getDisputeCount(Dispute.DisputeFilter disputeFilter);

}
