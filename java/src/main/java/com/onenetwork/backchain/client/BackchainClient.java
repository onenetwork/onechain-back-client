package com.onenetwork.backchain.client;

/**
 * Interface for interacting with the Backchain.  Acquire an instance
 * via {@link BackchainClientFactory}.  
 */
public interface BackchainClient {

	/**
	 * Hashes are stored to the Backchain sequentially in insertion order. 
	 * This method returns the hash stored at the given index (starting from zero).
	 * 
	 * @param index index of the desired hash (starting from zero)
	 * @return hash at the given index
	 */
	String getHash(long index);
	
	/**
	 * Returns the address of the entity acting as the Orchestrator for the Backchain.
	 * 
	 * @return address of the entity acting as the Orchestrator for the Backchain
	 */
	String getOrchestrator() ;

	/**
	 * Returns the total number of hashes stored in the backchain. 
	 * See also @{link #getHash(index)}
	 * 
	 * @return total number of hashes stored in the backchain
	 */
	long hashCount();

	/**
	 * Posts a new hash to the Backchain. 
	 * Available only to the Orchestrator. 
	 * 
	 * @param hash new hash to the Backchain
	 */
	void post(String hash);

	/**
	 * Returns true if the given hash is present in the Backchain, false otherwise.
	 * 
	 * @param hash has to be verified
	 * @return true if the given hash is present in the Backchain, false otherwise
	 */
	boolean verify(String hash);
	
}
