package com.onenetwork.backchain.client.eth;

import java.math.BigInteger;

import com.onenetwork.backchain.client.BackchainClientConfig;

public class EthereumConfig implements BackchainClientConfig {

	private String url;
	private String contractAddress;
	private String privateKey;
	private BigInteger gasPrice = BigInteger.valueOf(0);
	private BigInteger gasLimit = BigInteger.valueOf(0);

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

	public BigInteger getGasPrice() {
		return gasPrice;
	}

	public EthereumConfig setGasPrice(BigInteger gasPrice) {
		this.gasPrice = gasPrice;
		return this;
	}

	public BigInteger getGasLimit() {
		return gasLimit;
	}

	public EthereumConfig setGasLimit(BigInteger gasLimit) {
		this.gasLimit = gasLimit;
		return this;
	}

}
