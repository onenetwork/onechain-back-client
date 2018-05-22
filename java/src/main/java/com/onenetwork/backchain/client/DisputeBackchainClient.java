package com.onenetwork.backchain.client;

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
   * Close the {@link Dispute} with matching dispueID
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
   * Get the Dispute submission window
   * @return time in minutes
   */
  int getDisputeSubmissionWindowInMinutes();

  
  /**
   * set the Dispute submission window
   * Available only to the Orchestrator.
   * 
   * @param timeInMinutes 
   */
  void setDisputeSubmissionWindowInMinutes(int timeInMinutes);
  
  /**
   * get the filter out {@link Dispute} array
   * @param disputeFilter 
   * @return array of {@link Dispute}
   */
  Dispute[] filterDisputes(Dispute.DisputeFilter disputeFilter);
  
  /**
   * get the count of dispute matching 
   * @param disputeFilter
   * @return
   */
  int getDisputeCount(Dispute.DisputeFilter disputeFilter);

}
