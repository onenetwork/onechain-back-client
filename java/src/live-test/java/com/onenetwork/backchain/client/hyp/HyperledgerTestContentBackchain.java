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
		//assertEquals("0xece1355c30af00ff4f03f0e37f7822ce4b660aa3", bk.getOrchestrator());

		
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
