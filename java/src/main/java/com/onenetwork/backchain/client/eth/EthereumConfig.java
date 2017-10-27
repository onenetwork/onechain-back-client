package com.onenetwork.backchain.client.eth;

import com.onenetwork.backchain.client.BackchainClientConfig;

public class EthereumConfig implements BackchainClientConfig {

	private String url;
	private String contractAddress;
	private String privateKey;

	public String getUrl() {
		return url;
	}

	public EthereumConfig setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getContractAddress() {
		return contractAddress;
	}

	public EthereumConfig setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
		return this;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public EthereumConfig setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
		return this;
	}

}
