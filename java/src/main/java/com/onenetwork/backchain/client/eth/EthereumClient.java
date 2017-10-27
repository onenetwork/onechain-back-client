package com.onenetwork.backchain.client.eth;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import com.onenetwork.backchain.client.BackchainClient;

public class EthereumClient implements BackchainClient {

	private EthereumConfig config;
	private Credentials credentials;
	private Web3j web3;

	public EthereumClient(EthereumConfig config) {
		this.config = config;
		
		web3 = Web3j.build(new HttpService(config.getUrl()));
		credentials = Credentials.create(config.getPrivateKey());
	}

	@Override
	public String getHash(long index) {
		return null;
	}

	@Override
	public String getOrchestrator() {
		return null;
	}

	@Override
	public int hashCount() {
		return 0;
	}

	@Override
	public void post(String hash) {
	}

	@Override
	public boolean verify(String hash) {
		return false;
	}

}
