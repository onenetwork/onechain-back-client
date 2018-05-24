package com.onenetwork.backchain.client;

import java.util.Calendar;

/**
 * A Dispute captures a concern raised by a Backchain participant about a
 * transaction or business transaction.  Any participant can file a Dispute,
 * and only that participant can "resolve" the Dispute. 
 */
public class Dispute {

  /**
   * Returns the globally unique identifier for the Dispute.
   * 
   * @return globally unique identifier for the Dispute. 
   */
  public String getDisputeID() {
    return disputeID;
  }

  /**
   * Returns the address of the entity who filed the Dispute.
   * 
   * @return address of the entity who filed the Dispute.
   */
  public String getDisputingParty() {
    return disputingParty;
  }

  /**
   * Returns the Transaction against which the Dispute is being registered.
   * 
   * @return the Transaction against which the Dispute is being registered.
   */
  public String getDisputedTransactionID() {
    return disputedTransactionID;
  }

  /**
   * Returns the IDs of specific Business Transactions within the Disputed Transaction which
   * are in dispute.
   * 
   * @return the IDs of specific Business Transactions within the Disputed Transaction which
   * are in dispute.
   */
  public String[] getDisputedBusinessTransactionIDs() {
    return disputedBusinessTransactionIDs;
  }

  /**
   * Returns the date when the Dispute was submitted to the Backchain.
   * 
   * @return the date when the Dispute was submitted to the Backchain.
   */
  public Calendar getSubmittedDate() {
    return submittedDate;
  }

  /**
   * Returns the date when the Dispute was closed.  (May be null)
   * 
   * @return the date when the Dispute was closed.  (May be null)
   */
  public Calendar getCloseDate() {
    return closeDate;
  }

  /**
   * Returns the state of the Dispute.
   * 
   * @return the state of the Dispute.
   */
  public State getState() {
    return state;
  }

  /**
   * Returns the reason code describing why the Dispute was filed.
   * 
   * @return the reason code describing why the Dispute was filed.
   */
  public Reason getReason() {
    return reason;
  }

  /**
   * @param disputeID globally unique identifier for the Dispute
   * @return self (builder pattern)
   */
  public Dispute setDisputeID(String disputeID) {
    this.disputeID = disputeID;
    return this;
  }

  /**
   * @param disputingParty address of the entity who filed the Dispute.
   * @return self (builder pattern)
   */
  public Dispute setDisputingParty(String disputingParty) {
    this.disputingParty = disputingParty;
    return this;
  }

  /**
   * @param disputedTransactionID the Transaction against which the Dispute is being registered.
   * @return self (builder pattern)
   */
  public Dispute setDisputedTransactionID(String disputedTransactionID) {
    this.disputedTransactionID = disputedTransactionID;
    return this;
  }

  /**
   * @param disputedBusinessTransactionIDs the IDs of specific Business Transactions within the Disputed Transaction which
   * are in dispute.
   * @return self (builder pattern)
   */
  public Dispute setDisputedBusinessTransactionIDs(String[] disputedBusinessTransactionIDs) {
    this.disputedBusinessTransactionIDs = disputedBusinessTransactionIDs;
    return this;
  }

  /**
   * @param submittedDate the date when the Dispute was submitted to the Backchain.
   * @return self (builder pattern)
   */
  public Dispute setSubmittedDate(Calendar submittedDate) {
    this.submittedDate = submittedDate;
    return this;
  }

  /**
   * @param closeDate the date when the Dispute was closed.  (May be null)
   * @return self (builder pattern)
   */
  public Dispute setCloseDate(Calendar closeDate) {
    this.closeDate = closeDate;
    return this;
  }

  /**
   * @param state the state of the Dispute.
   * @return self (builder pattern)
   */
  public Dispute setState(State state) {
    this.state = state;
    return this;
  }

  /**
   * @param reason the reason code describing why the Dispute was filed.
   * @return self (builder pattern)
   */
  public Dispute setReason(Reason reason) {
    this.reason = reason;
    return this;
  }


  /**
   * Describes all possible states in the lifecycle of the Dispute.  
   */
  public static enum State {
    OPEN, CLOSED;
 }

  /**
   * Describes all reason code for which a Dispute may be filed.
   */
  public static enum Reason {
    HASH_NOT_FOUND, INPUT_DISPUTED, TRANSACTION_DATE_DISPUTED, TRANSACTION_PARTIES_DISPUTED, DISPUTE_BUSINESS_TRANSACTIONS, FINANCIAL_DISPUTED;
  }
  
  /**
   * Object used for querying Disputes.
   * Getters/setters are provide for setting various values to be provided
   * to the search API.
   * This class uses a "builder" pattern so you can chain set calls together in a single line.
   */
  public static class DisputeFilter {
	  
    public String[] getDisputeIDs() {
      return disputeIDs;
    }

    public String[] getDisputingParties() {
      return disputingParties;
    }

    public String[] getDisputedTransactionIDs() {
      return disputedTransactionIDs;
    }

    public String[] getDisputedBusinessTransactionIDs() {
      return disputedBusinessTransactionIDs;
    }

    public Calendar getSubmittedStartDate() {
      return submittedStartDate;
    }

    public Calendar getSubmittedEndDate() {
      return submittedEndDate;
    }

    public Calendar getCloseStartDate() {
      return closeStartDate;
    }

    public Calendar getCloseEndDate() {
      return closeEndDate;
    }

    public State[] getStates() {
      return states;
    }

    public Reason[] getReasons() {
      return reasons;
    }

    public DisputeFilter setDisputeIDs(String[] disputeIDs) {
      this.disputeIDs = disputeIDs;
      return this;
    }

    public DisputeFilter setDisputingParties(String[] disputingParties) {
      this.disputingParties = disputingParties;
      return this;
    }

    public DisputeFilter setDisputedTransactionIDs(String[] disputedTransactionIDs) {
      this.disputedTransactionIDs = disputedTransactionIDs;
      return this;
    }

    public DisputeFilter setDisputedBusinessTransactionIDs(String[] disputedBusinessTransactionIDs) {
      this.disputedBusinessTransactionIDs = disputedBusinessTransactionIDs;
      return this;
    }

    public DisputeFilter setSubmittedStartDate(Calendar submittedStartDate) {
      this.submittedStartDate = submittedStartDate;
      return this;
    }

    public DisputeFilter setSubmittedEndDate(Calendar submittedEndDate) {
      this.submittedEndDate = submittedEndDate;
      return this;
    }

    public DisputeFilter setCloseStartDate(Calendar closeStartDate) {
      this.closeStartDate = closeStartDate;
      return this;
    }

    public DisputeFilter setCloseEndDate(Calendar closeEndDate) {
      this.closeEndDate = closeEndDate;
      return this;
    }

    public DisputeFilter setStates(State[] states) {
      this.states = states;
      return this;
    }

    public DisputeFilter setReasons(Reason[] reasons) {
      this.reasons = reasons;
      return this;
    }

    private String[] disputeIDs;
    private String[] disputingParties;
    private String[] disputedTransactionIDs;
    private String[] disputedBusinessTransactionIDs;
    private Calendar submittedStartDate;
    private Calendar submittedEndDate;
    private Calendar closeStartDate;
    private Calendar closeEndDate;
    private State[] states;
    private Reason[] reasons;
  }

  private String disputeID;
  private String disputingParty;
  private String disputedTransactionID;
  private String[] disputedBusinessTransactionIDs;
  private Calendar submittedDate;
  private Calendar closeDate;
  private State state;
  private Reason reason;

}
