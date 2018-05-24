package com.onenetwork.backchain.client.eth;

import java.math.BigInteger;

import com.onenetwork.backchain.client.BackchainClientConfig;

/**
 * Implementation of {@link BackchainClientConfig} for an
 * <a href="https://www.ethereum.org/">Ethereum</a>-based Backchain.
 */
public class EthereumConfig implements BackchainClientConfig {

	private String url;
	private String contentBackchainContractAddress;
	private String disputeBackchainContractAddress;
	private String privateKey;
	private BigInteger gasPrice = BigInteger.valueOf(0);
	private BigInteger gasLimit = BigInteger.valueOf(0);

	/**
	 * Returns the HTTP URL of the Ethereum backchain
	 * @return HTTP URL of the Ethereum backchain
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the HTTP URL of the Ethereum backchain
	 * @param url HTTP URL of the Ethereum backchain
	 * @return this config object
	 */
	public EthereumConfig setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * Returns the Ethereum smart contract address of ContentBackchain, as a base64-encoded string
	 * starting with "0x".  For example, "0xc5d4b021858a17828532e484b915149af5e1b138"
	 * @return Ethereum smart contract address, as a base64-encoded string starting with "0x"
	 */
	public String getContentBackchainContractAddress() {
		return contentBackchainContractAddress;
	}

	/**
	 * Sets the Ethereum smart contract address for ContentBackchain.  Expects a base64-encoded string
	 * starting with "0x".  For example, "0xc5d4b021858a17828532e484b915149af5e1b138"
	 * @param address base64-encoded string starting with "0x", for example, "0xc5d4b021858a17828532e484b915149af5e1b138"
	 * @return this config object
	 */
	public EthereumConfig setContentBackchainContractAddress(String address) {
		this.contentBackchainContractAddress = address;
		return this;
	}

  /**
   * Returns the Ethereum smart contract address of DisputeBackchain, as a base64-encoded string
   * starting with "0x".  For example, "0xc5d4b021858a17828532e484b915149af5e1b138"
   * @return Ethereum smart contract address, as a base64-encoded string starting with "0x"
   */
  public String getDisputeBackchainContractAddress() {
    return disputeBackchainContractAddress;
  }

  /**
   * Sets the Ethereum smart contract address for DisputeBackchain.  Expects a base64-encoded string
   * starting with "0x".  For example, "0xc5d4b021858a17828532e484b915149af5e1b138"
   * @param address base64-encoded string starting with "0x", for example, "0xc5d4b021858a17828532e484b915149af5e1b138"
   * @return this config object
   */
  public EthereumConfig setDisputeBackchainContractAddress(String address) {
    this.disputeBackchainContractAddress = address;
    return this;
  }

	/**
	 * Returns the private key of the Ethereum client, as a base64-encoded string
	 * starting with "0x".  For example, "0x8ad0132f808d0830c533d7673cd689b7fde2d349ff0610e5c04ceb9d6efb4eb1"
	 * @return private key of the Ethereum client, as a base64-encoded string starting with "0x"
	 */
	public String getPrivateKey() {
		return privateKey;
	}

	/**
	 * Sets the private key of the Ethereum client, as a base64-encoded string
	 * starting with "0x".  For example, "0x8ad0132f808d0830c533d7673cd689b7fde2d349ff0610e5c04ceb9d6efb4eb1"
	 * Needed only if you intend to post to the backchain.  If you are only verifying, you can skip this.
	 * @param privateKey private key of the Ethereum client, as a base64-encoded string starting with "0x"
	 * @return this config object
	 */
	public EthereumConfig setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
		return this;
	}

	/**
	 * Sets the max gas price the client is willing to pay
	 * @return max gas price the client is willing to pay
	 */
	public BigInteger getGasPrice() {
		return gasPrice;
	}

	/**
	 * Sets the max gas price the client is willing to pay
	 * @param gasPrice max gas price the client is willing to pay
	 * @return this config object
	 */
	public EthereumConfig setGasPrice(BigInteger gasPrice) {
		this.gasPrice = gasPrice;
		return this;
	}

	/**
	 * Returns the max amount of gas the client is willing to use
	 * @return max amount of gas the client is willing to use
	 */
	public BigInteger getGasLimit() {
		return gasLimit;
	}

	/**
	 * Sets the max amount of gas the client is willing to use
	 * @param gasLimit max amount of gas the client is willing to use
	 * @return this config object
	 */
	public EthereumConfig setGasLimit(BigInteger gasLimit) {
		this.gasLimit = gasLimit;
		return this;
	}

}
