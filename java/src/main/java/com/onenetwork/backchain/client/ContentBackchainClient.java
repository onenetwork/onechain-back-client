package com.onenetwork.backchain.client;

/**
 * Interface for interacting with the ContentBackchain.  Acquire an instance
 * via {@link BackchainClientFactory}.  
 */
public interface ContentBackchainClient {

	/**
	 * Hashes are stored to the ContentBackchain sequentially in insertion order. 
	 * This method returns the hash stored at the given index (starting from zero).
	 * 
	 * @param index index of the desired hash (starting from zero)
	 * @return hash at the given index
	 */
	String getHash(long index);
	
	/**
	 * Returns the address of the entity acting as the Orchestrator for the ContentBackchain.
	 * 
	 * @return address of the entity acting as the Orchestrator for the ContentBackchain
	 */
	String getOrchestrator() ;

	/**
	 * Returns the total number of hashes stored in the content backchain. 
	 * See also @{link #getHash(index)}
	 * 
	 * @return total number of hashes stored in the content backchain
	 */
	long hashCount();

	/**
	 * Posts a new hash to the ContentBackchain. 
	 * Available only to the Orchestrator. 
	 * 
	 * @param hash new hash to the ContentBackchain
	 */
	void post(String hash);

	/**
	 * Returns true if the given hash is present in the ContentBackchain, false otherwise.
	 * 
	 * @param hash has to be verified
	 * @return true if the given hash is present in the ContentBackchain, false otherwise
	 */
	boolean verify(String hash);
	
}
