/**
 * Copyright (c) 2003-2018, Parmod Singh Rana, All rights reserved.
 */
package com.onenetwork.backchain.client.eth;

/**
 * TODO complete the class documentation
 *
 * @author parmodsinghrana
 *
 */

import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class TestMethod {

  // https://www.reddit.com/r/ethereum/comments/5g8ia6/attention_miners_we_recommend_raising_gas_limit/
  static final BigInteger GAS_PRICE = BigInteger.valueOf(20_000_000_000L);
  static final BigInteger GAS_LIMIT = BigInteger.valueOf(1000000l);
  static final BigInteger INITIAL_WEI = BigInteger.valueOf(40000);

  protected Credentials alice;

  protected Credentials bob;

  private static final String PRIVATE_KEY_STRING = "0x8ad0132f808d0830c533d7673cd689b7fde2d349ff0610e5c04ceb9d6efb4eb1";

  @Before
  public void initialize() {
    alice = Credentials.create(PRIVATE_KEY_STRING);
    bob = Credentials.create(
      "0xcacb0026806d19eb063006dbf1dc2cb952808c17b436f5dd27f283babd60bc37", // 32
                                                                            // byte
                                                                            // hex
      "0x0002436ae129062b7ea2457aae7a144e38af8ad3" // 64 byte hex
    );
  }

  @Test
  public void testDeploy() throws InterruptedException, ExecutionException {
    Web3j web3j = Web3j.build(new HttpService("http://192.168.201.55:8545"));
    Future<DisputeBackchainABI> underTest = DisputeBackchainABI.deploy(web3j, alice, GAS_PRICE, GAS_LIMIT, INITIAL_WEI);
    int i = 1000;
    while (!underTest.isDone()) {
      if (--i < 0) {
        fail("that is slow!");
      }
      Thread.sleep(10l);
    }
    DisputeBackchainABI helloMe = underTest.get();
    Future<Address> result = helloMe.getOrchestrator();
    System.out.println(helloMe.getOrchestrator());
  }

}
