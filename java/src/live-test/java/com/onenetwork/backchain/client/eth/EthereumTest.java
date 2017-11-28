package com.onenetwork.backchain.client.eth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jcajce.provider.digest.SHA224.Digest;
import org.junit.Test;

import com.onenetwork.backchain.client.BackchainClient;
import com.onenetwork.backchain.client.BackchainClientFactory;

public class EthereumTest {

	private String newHash() throws Exception {
		return "0x" + Hex.encodeHexString(
				Digest.getInstance("SHA256").digest(("" + new Date().getTime() + Math.random()).getBytes()));
	}

	@Test
	public void testOrchestrator() throws Exception {
		EthereumConfig cfg = new EthereumConfig().setUrl("http://backchain-vagrant.onenetwork.com:8545")
				.setContractAddress("0xc5d4b021858a17828532e484b915149af5e1b138")
				.setPrivateKey("0x8ad0132f808d0830c533d7673cd689b7fde2d349ff0610e5c04ceb9d6efb4eb1")
				.setGasPrice(BigInteger.valueOf(0L)).setGasLimit(BigInteger.valueOf(999999L));
		BackchainClient bk = BackchainClientFactory.newBackchainClient(cfg);

		long initialHashCount = bk.hashCount();
		assertEquals("0xece1355c30af00ff4f03f0e37f7822ce4b660aa3", bk.getOrchestrator());

		String hash = newHash();
		bk.post(hash);
		long newHashCount = bk.hashCount();
		assertEquals(initialHashCount + 1, newHashCount);
		assertTrue(bk.verify(hash));
		assertEquals(hash, bk.getHash(newHashCount - 1));

		hash = newHash();
		assertFalse(bk.verify(hash));
	}

	@Test
	public void testParticipant() throws Exception {
		EthereumConfig cfg = new EthereumConfig().setUrl("http://backchain-vagrant.onenetwork.com:8545")
				.setContractAddress("0xc5d4b021858a17828532e484b915149af5e1b138")
				.setPrivateKey("0x69bc764651de75758c489372c694a39aa890f911ba5379caadc08f44f8173051")
				.setGasPrice(BigInteger.valueOf(0L)).setGasLimit(BigInteger.valueOf(999999L));
		BackchainClient bk = BackchainClientFactory.newBackchainClient(cfg);

		bk.hashCount();
		assertEquals("0xece1355c30af00ff4f03f0e37f7822ce4b660aa3", bk.getOrchestrator());

		try {
			bk.post(newHash());
			fail("Exception anticipated");
		} catch (Exception e) {
			// Test passed
		}
	}

	@Test
	public void testUnableToConnect() throws Exception {
		EthereumConfig cfg = new EthereumConfig().setUrl("http://backchain-vagrant.onenetwork.com:8546")
				.setContractAddress("0xc5d4b021858a17828532e484b915149af5e1b138")
				.setPrivateKey("0x8ad0132f808d0830c533d7673cd689b7fde2d349ff0610e5c04ceb9d6efb4eb1")
				.setGasPrice(BigInteger.valueOf(0L)).setGasLimit(BigInteger.valueOf(999999L));
		BackchainClient bk = BackchainClientFactory.newBackchainClient(cfg);

		try {
			bk.hashCount();
			fail("Exception anticipated");
		} catch (Exception e) {
			// Test passed
		}
	}
}
