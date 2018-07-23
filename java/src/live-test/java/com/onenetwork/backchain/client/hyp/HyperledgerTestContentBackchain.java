package com.onenetwork.backchain.client.hyp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.MessageDigest;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.onenetwork.backchain.client.BackchainClientFactory;
import com.onenetwork.backchain.client.ContentBackchainClient;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) // testOrchestrator() should be executed before testParticipant() so that hash will present in ledger for verification
public class HyperledgerTestContentBackchain {
  
  private static final String ORCHESTRATOR = "0x2d2d2d2d2d424547494e205055424c4943204b45592d2d2d2d2d0d0a4d466b77457759484b6f5a497a6a3043415159494b6f5a497a6a3044415163445167414536486f474a3564616c456a47417950476777654c674c365054326c4c0d0a635675344c6272734e316d62675378457658344d78493371647467545164493845305253734949746a696e74714c2b6f562f6f574b78383836413d3d0d0a2d2d2d2d2d454e44205055424c4943204b45592d2d2d2d2d0d0a";
private String orchestratorKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Ik9yY2hlc3RyYXRvclVzZXIiLCJvcmdOYW1lIjoiT3JjaGVzdHJhdG9yT3JnIiwiaWF0IjoxNTMyMDAxMzgzfQ.Wy8RITjRfd3O5wvvoUr7ReIMHtsdpYO6nB385vXIwTU";
  private String participantKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlBhcnRpY2lwYW50VXNlciIsIm9yZ05hbWUiOiJQYXJ0aWNpcGFudE9yZyIsImlhdCI6MTUzMjAwMTM5MH0.Jw3bgaoOHKs6pJmsTF97mJRnoq3GCyyDW0QbLeSBAfU";
  private final String sampleHash = "0x0506d0e67v3e80ndr4a441f9c8344f93d8351e6a29b00fe249d66b1fce321bbb";
	  
	private String newHash() throws Exception {
		return "0x" + Hex.encodeHexString(
		  MessageDigest.getInstance("SHA-256").digest(("" + new Date().getTime() + Math.random()).getBytes()));
	}

	@Test
	public void testOrchestrator() throws Exception { 
		HyperledgerConfig cfg = new HyperledgerConfig().setUrl("http://localhost:4000").setToken(orchestratorKey);

	    ContentBackchainClient bk = BackchainClientFactory.newContentBackchainClient(cfg);

		long initialHashCount = bk.hashCount();
		assertEquals(ORCHESTRATOR, bk.getOrchestrator());

		
		bk.post(sampleHash);
		long newHashCount = bk.hashCount();
		assertEquals(initialHashCount + 1, newHashCount);
		assertTrue(bk.verify(sampleHash));

		String hash = newHash();
		assertFalse(bk.verify(hash));
	}

	@Test
	public void testParticipant() throws Exception {
		HyperledgerConfig cfg = new HyperledgerConfig().setUrl("http://localhost:4000").setToken(participantKey);
		ContentBackchainClient bk = BackchainClientFactory.newContentBackchainClient(cfg);

		assertTrue(bk.hashCount() > 0);
		assertTrue(bk.verify(sampleHash));
		assertFalse(bk.verify(newHash()));
		
		assertEquals(ORCHESTRATOR, bk.getOrchestrator());

		try {
			bk.post(newHash());
			fail("Exception anticipated");
		} catch (Exception e) {
			// Test passed
		}
	}

	@Test
	public void testUnableToConnect() throws Exception {
	  HyperledgerConfig cfg = new HyperledgerConfig().setUrl("http://backchain-vagrant.onenetwork.com:4000")
      .setToken(orchestratorKey);
		ContentBackchainClient bk = BackchainClientFactory.newContentBackchainClient(cfg);

		try {
			bk.hashCount();
			fail("Exception anticipated");
		} catch (Exception e) {
			// Test passed
		}
	}
}
