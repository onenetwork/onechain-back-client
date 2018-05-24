package com.onenetwork.backchain.client;

/**
 * Marker interface for capturing the configuration needed to connect
 * to a particular Backchain implementation.  Must be provided to
 * {@link BackchainClientFactory} to get a {@link ContentBackchainClient} or {@link DisputeBackchainClient} 
 * instance.
 */
public interface BackchainClientConfig {

}
