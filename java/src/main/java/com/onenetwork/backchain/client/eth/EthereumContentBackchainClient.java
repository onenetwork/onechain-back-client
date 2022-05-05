package com.onenetwork.backchain.client.eth;

import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;

import com.onenetwork.backchain.client.BackchainClientConfig;
import com.onenetwork.backchain.client.ContentBackchainClient;

/**
 * Implementation of {@link ContentBackchainClient} for an
 * <a href="https://www.ethereum.org/">Ethereum</a>-based Backchain.
 */
public class EthereumContentBackchainClient implements ContentBackchainClient {

  private Web3j web3j;
  private ContentBackchainABI contentBackchainABI;

  /**
   * @param config Ethereum-based {@link BackchainClientConfig}
   */
  public EthereumContentBackchainClient(EthereumConfig config) {
    web3j = Web3j.build(new HttpService(config.getUrl()));
    if (config.getPrivateKey() != null) {
      Credentials credentials = Credentials.create(config.getPrivateKey());
      contentBackchainABI = ContentBackchainABI.load(config.getContentBackchainContractAddress(), web3j, credentials, config.getGasPrice(),
          config.getGasLimit());
    }
    else {
      ClientTransactionManager tm = new ClientTransactionManager(web3j, "0x00000000000000000000000000000000");
      contentBackchainABI = ContentBackchainABI.load(config.getContentBackchainContractAddress(), web3j, tm, config.getGasPrice(),
          config.getGasLimit());
    }
  }

  
  
  @Override
  public String getHash(long index) {
    try {
      return EthereumHelper.hashBytesToString(contentBackchainABI.getHash(new BigInteger("" + index)).send());
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getOrchestrator() {
    try {
      return contentBackchainABI.orchestrator().send();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public long hashCount() {
    try {
      return contentBackchainABI.hashCount().send().longValue();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void post(String hash) {
    try {
      contentBackchainABI.post(EthereumHelper.hashStringToBytes(hash)).send();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean verify(String hash) {
    try {
      return contentBackchainABI.verify(EthereumHelper.hashStringToBytes(hash)).send();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
