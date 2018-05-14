package com.onenetwork.backchain.client;

import com.onenetwork.backchain.client.eth.EthereumConfig;
import com.onenetwork.backchain.client.eth.EthereumContentBackchainClient;

/**
 * Factory for acquiring instances of {@link BackchainClient}. You will need to
 * provide a concrete implementation of {@link BackchainClientConfig} (for
 * example {@link EthereumConfig}), and will then receive in return a generic
 * {@link BackchainClient} you can use for interacting with the Backchain.
 */
public class BackchainClientFactory {

	/**
	 * Given a concrete implementation of {@link BackchainClientConfig} (for example
	 * {@link EthereumConfig}), returns a suitable {@link BackchainClient} instance.
	 * 
	 * @param config concrete implementation of {@link BackchainClientConfig} (for example {@link EthereumConfig})
	 * @return suitable {@link BackchainClient} instance
	 */
	public static ContentBackchainClient newContentBackchainClient(BackchainClientConfig config) {
		if (config instanceof EthereumConfig) {
			return new EthereumContentBackchainClient((EthereumConfig) config);
		}

		throw new IllegalArgumentException("Unsupported configuration: " + config);
	}

}
