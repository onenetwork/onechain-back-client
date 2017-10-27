package com.onenetwork.backchain.client;

import com.onenetwork.backchain.client.eth.EthereumClient;
import com.onenetwork.backchain.client.eth.EthereumConfig;

public class BackchainClientFactory {

	public static BackchainClient newBackchainClient(BackchainClientConfig config) {
		if (config instanceof EthereumConfig) {
			return new EthereumClient((EthereumConfig) config);
		}

		throw new IllegalArgumentException("Unsupported configuration: " + config);
	}

}
