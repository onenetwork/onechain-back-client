package com.onenetwork.backchain.client;

import com.onenetwork.backchain.client.eth.EthereumConfig;
import com.onenetwork.backchain.client.eth.EthereumContentBackchainClient;
import com.onenetwork.backchain.client.eth.EthereumDisputeBackchainClient;

/**
 * Factory for acquiring instances of {@link ContentBackchainClient} and 
 * {@link DisputeBackchainClient}. You will need to
 * provide a concrete implementation of {@link BackchainClientConfig} (for
 * example {@link EthereumConfig}), and will then receive in return a generic
 * {@link ContentBackchainClient} or {@link DisputeBackchainClient} 
 * you can use for interacting with the Backchain.
 */
public class BackchainClientFactory {

  /**
   * Given a concrete implementation of {@link BackchainClientConfig} (for example
   * {@link EthereumConfig}), returns a suitable {@link ContentBackchainClient} instance.
   * 
   * @param config concrete implementation of {@link BackchainClientConfig} (for example {@link EthereumConfig})
   * @return suitable {@link ContentBackchainClient} instance
   */
  public static ContentBackchainClient newContentBackchainClient(BackchainClientConfig config) {
    if (config instanceof EthereumConfig) {
      return new EthereumContentBackchainClient((EthereumConfig) config);
    }

    throw new IllegalArgumentException("Unsupported configuration: " + config);
  }

  /**
   * Given a concrete implementation of {@link BackchainClientConfig} (for example
   * {@link EthereumConfig}), returns a suitable {@link DisputeBackchainClient} instance.
   * 
   * @param config concrete implementation of {@link BackchainClientConfig} (for example {@link EthereumConfig})
   * @return suitable {@link DisputeBackchainClient} instance
   */
  public static DisputeBackchainClient newDisputeBackchainClient(BackchainClientConfig config) {
    if (config instanceof EthereumConfig) {
      return new EthereumDisputeBackchainClient((EthereumConfig) config);
    }

    throw new IllegalArgumentException("Unsupported configuration: " + config);
  }

}
