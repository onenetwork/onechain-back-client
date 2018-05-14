package com.onenetwork.backchain.client;

import java.util.Calendar;

/**
 * Dispute
 *
 */
public class Dispute {
  /**
   * @return the disputeId
   */
  public String getDisputeID() {
    return disputeID;
  }

  /**
   * @return the disputingParty
   */
  public String getDisputingParty() {
    return disputingParty;
  }

  /**
   * @return the disputedTransactionID
   */
  public String getDisputedTransactionID() {
    return disputedTransactionID;
  }

  /**
   * @return the disputedBusinessTransactionIDs
   */
  public String[] getDisputedBusinessTransactionIDs() {
    return disputedBusinessTransactionIDs;
  }

  /**
   * @return the submittedDate
   */
  public Calendar getSubmittedDate() {
    return submittedDate;
  }

  /**
   * @return the closeDate
   */
  public Calendar getCloseDate() {
    return closeDate;
  }

  /**
   * @return the state
   */
  public State getState() {
    return state;
  }

  /**
   * @return the reason
   */
  public Reason getReason() {
    return reason;
  }

  /**
   * @param disputeID the disputeID to set
   */
  public Dispute setDisputeID(String disputeID) {
    this.disputeID = disputeID;
    return this;
  }

  /**
   * @param disputingParty the disputingParty to set
   */
  public Dispute setDisputingParty(String disputingParty) {
    this.disputingParty = disputingParty;
    return this;
  }

  /**
   * @param disputedTransactionID the disputedTransactionID to set
   */
  public Dispute setDisputedTransactionID(String disputedTransactionID) {
    this.disputedTransactionID = disputedTransactionID;
    return this;
  }

  /**
   * @param disputedBusinessTransactionIDs the disputedBusinessTransactionIDs to set
   */
  public Dispute setDisputedBusinessTransactionIDs(String[] disputedBusinessTransactionIDs) {
    this.disputedBusinessTransactionIDs = disputedBusinessTransactionIDs;
    return this;
  }

  /**
   * @param submittedDate the submittedDate to set
   */
  public Dispute setSubmittedDate(Calendar submittedDate) {
    this.submittedDate = submittedDate;
    return this;
  }

  public Dispute setCloseDate(Calendar closeDate) {
    this.closeDate = closeDate;
    return this;
  }

  /**
   * @param state the state to set
   */
  public Dispute setState(State state) {
    this.state = state;
    return this;
  }

  /**
   * @param reason the reason to set
   */
  public Dispute setReason(Reason reason) {
    this.reason = reason;
    return this;
  }


  public static enum State {
    OPEN, CLOSED;
 }

  public static enum Reason {
    HASH_NOT_FOUND, INPUT_DISPUTED, TRANSACTION_DATE_DISPUTED, TRANSACTION_PARTIES_DISPUTED, DISPUTE_BUSINESS_TRANSACTIONS, FINANCIAL_DISPUTED;
  }
  
  public static class DisputeFilter {
    /**
     * @return the disputeIDs
     */
    public String[] getDisputeIDs() {
      return disputeIDs;
    }
    /**
     * @return the disputingParties
     */
    public String[] getDisputingParties() {
      return disputingParties;
    }
    /**
     * @return the disputedTransactionIDs
     */
    public String[] getDisputedTransactionIDs() {
      return disputedTransactionIDs;
    }
    /**
     * @return the disputedBusinessTransactionIDs
     */
    public String[] getDisputedBusinessTransactionIDs() {
      return disputedBusinessTransactionIDs;
    }
    /**
     * @return the submittedStartDate
     */
    public Calendar getSubmittedStartDate() {
      return submittedStartDate;
    }
    /**
     * @return the submittedEndDate
     */
    public Calendar getSubmittedEndDate() {
      return submittedEndDate;
    }
    /**
     * @return the closeStartDate
     */
    public Calendar getCloseStartDate() {
      return closeStartDate;
    }
    /**
     * @return the closeEndDate
     */
    public Calendar getCloseEndDate() {
      return closeEndDate;
    }
    /**
     * @return the states
     */
    public State[] getStates() {
      return states;
    }
    /**
     * @return the reasons
     */
    public Reason[] getReasons() {
      return reasons;
    }
    /**
     * @param disputeIDs the disputeIDs to set
     * @return ${Dispute.DisputeFilter}
     */
    public DisputeFilter setDisputeIDs(String[] disputeIDs) {
      this.disputeIDs = disputeIDs;
      return this;
    }
    /**
     * @param disputingParties the disputingParties to set
     * @return ${Dispute.DisputeFilter}
     */
    public DisputeFilter setDisputingParties(String[] disputingParties) {
      this.disputingParties = disputingParties;
      return this;
    }
    /**
     * @param disputedTransactionIDs the disputedTransactionIDs to set
     * @return ${Dispute.DisputeFilter}
     */
    public DisputeFilter setDisputedTransactionIDs(String[] disputedTransactionIDs) {
      this.disputedTransactionIDs = disputedTransactionIDs;
      return this;
    }
    /**
     * @param disputedBusinessTransactionIDs the disputedBusinessTransactionIDs to set
     * @return ${Dispute.DisputeFilter}
     */
    public DisputeFilter setDisputedBusinessTransactionIDs(String[] disputedBusinessTransactionIDs) {
      this.disputedBusinessTransactionIDs = disputedBusinessTransactionIDs;
      return this;
    }
    /**
     * @param submittedStartDate the submittedStartDate to set
     * @return ${Dispute.DisputeFilter}
     */
    public DisputeFilter setSubmittedStartDate(Calendar submittedStartDate) {
      this.submittedStartDate = submittedStartDate;
      return this;
    }
    /**
     * @param submittedEndDate the submittedEndDate to set
     * @return ${Dispute.DisputeFilter}
     */
    public DisputeFilter setSubmittedEndDate(Calendar submittedEndDate) {
      this.submittedEndDate = submittedEndDate;
      return this;
    }
    /**
     * @param closeStartDate the closeStartDate to set
     * @return ${Dispute.DisputeFilter}
     */
    public DisputeFilter setCloseStartDate(Calendar closeStartDate) {
      this.closeStartDate = closeStartDate;
      return this;
    }
    /**
     * @param closeEndDate the closeEndDate to set
     * @return ${Dispute.DisputeFilter}
     */
    public DisputeFilter setCloseEndDate(Calendar closeEndDate) {
      this.closeEndDate = closeEndDate;
      return this;
    }
    /**
     * @param states the states to set
     * @return ${Dispute.DisputeFilter}
     */
    public DisputeFilter setStates(State[] states) {
      this.states = states;
      return this;
    }
    /**
     * @param reasons the reasons to set
     * @return ${Dispute.DisputeFilter}
     */
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
