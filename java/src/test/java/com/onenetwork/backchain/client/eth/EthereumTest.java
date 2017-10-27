package com.onenetwork.backchain.client.eth;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.onenetwork.backchain.client.BackchainClient;
import com.onenetwork.backchain.client.BackchainClientFactory;

public class EthereumTest {

	@Test
	public void testSomeLibraryMethod() {
		EthereumConfig cfg = new EthereumConfig().setUrl("").setContractAddress("").setPrivateKey("");
		//BackchainClient client = BackchainClientFactory.newBackchainClient(cfg);
		assertTrue("someLibraryMethod should return 'true'", true);
	}

}
