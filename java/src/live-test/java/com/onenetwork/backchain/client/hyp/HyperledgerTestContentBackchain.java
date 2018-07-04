package com.onenetwork.backchain.client.hyp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jcajce.provider.digest.SHA224.Digest;
import org.junit.Test;

import com.onenetwork.backchain.client.BackchainClientFactory;
import com.onenetwork.backchain.client.ContentBackchainClient;
import com.onenetwork.backchain.client.eth.EthereumConfig;

public class HyperledgerTestContentBackchain {
  
  private String orchestratorKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MzA3MjAzMDQsInVzZXJuYW1lIjoiT3JjaGVzdHJhdG9yVXNlciIsIm9yZ05hbWUiOiJPcmNoZXN0cmF0b3JPcmciLCJpYXQiOjE1MzA2ODQzMDR9.s8ECySgPEmp-7Joups39_LX7s8nyTkBbNAgv-r8UBxU";
  private String participantKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MzA3MjAzMTAsInVzZXJuYW1lIjoiUGFydGljaXBhbnRVc2VyIiwib3JnTmFtZSI6IlBhcnRpY2lwYW50T3JnIiwiaWF0IjoxNTMwNjg0MzEwfQ.8UZN1_jWFbAIkbd37HXD1CSALVi9uohNF0eYY-20ptM";

	private String newHash() throws Exception {
		return "0x" + Hex.encodeHexString(
		  MessageDigest.getInstance("SHA-256").digest(("" + new Date().getTime() + Math.random()).getBytes()));
	}

	@Test
	public void testOrchestrator() throws Exception { 
		HyperledgerConfig cfg = new HyperledgerConfig().setUrl("http://192.168.201.55:4000").setPrivateKey(orchestratorKey);

	    ContentBackchainClient bk = BackchainClientFactory.newContentBackchainClient(cfg);

		long initialHashCount = bk.hashCount();
		//assertEquals("0xece1355c30af00ff4f03f0e37f7822ce4b660aa3", bk.getOrchestrator());

		String hash = newHash();
		bk.post(hash);
		long newHashCount = bk.hashCount();
		assertEquals(initialHashCount + 1, newHashCount);
		assertTrue(bk.verify(hash));

		hash = newHash();
		assertFalse(bk.verify(hash));
	}

	@Test
	public void testParticipant() throws Exception {
		HyperledgerConfig cfg = new HyperledgerConfig().setUrl("http://192.168.201.55:4000").setPrivateKey(participantKey);
		ContentBackchainClient bk = BackchainClientFactory.newContentBackchainClient(cfg);

		bk.hashCount();
		//assertEquals("0xece1355c30af00ff4f03f0e37f7822ce4b660aa3", bk.getOrchestrator());

		try {
			bk.post(newHash());
			fail("Exception anticipated");
		} catch (Exception e) {
			// Test passed
		}
	}

	@Test
	public void testUnableToConnect() throws Exception {
	  HyperledgerConfig cfg = new HyperledgerConfig().setUrl("http://192.168.201.55:4000")
      .setPrivateKey(orchestratorKey);
		ContentBackchainClient bk = BackchainClientFactory.newContentBackchainClient(cfg);

		try {
			bk.hashCount();
			fail("Exception anticipated");
		} catch (Exception e) {
			// Test passed
		}
	}
}
