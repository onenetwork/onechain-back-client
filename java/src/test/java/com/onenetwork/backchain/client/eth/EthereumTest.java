package com.onenetwork.backchain.client.eth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
	public void testEthereumBackchain() throws Exception {
		EthereumConfig cfg = new EthereumConfig().setUrl("http://55.55.55.55:8545")
				.setContractAddress("0x67932516a4d96b4c17122e23110e52240949c8ec")
				.setPrivateKey("0x6c00cc9e01a29ad8fd918ba26a3d4aea1bb22efc06347d78bc18a6216798328a")
				.setGasPrice(BigInteger.valueOf(0L)).setGasLimit(BigInteger.valueOf(999999L));
		BackchainClient bk = BackchainClientFactory.newBackchainClient(cfg);

		long initialHashCount = bk.hashCount();
		assertEquals("0xff17190af4e2a747f32ec9fe0ad49867fefe0931", bk.getOrchestrator());

		String hash = newHash();
		bk.post(hash);
		long newHashCount = bk.hashCount();
		assertEquals(initialHashCount + 1, newHashCount);
		assertTrue(bk.verify(hash));
		assertEquals(hash, bk.getHash(newHashCount - 1));

		hash = newHash();
		assertFalse(bk.verify(hash));
	}
}
