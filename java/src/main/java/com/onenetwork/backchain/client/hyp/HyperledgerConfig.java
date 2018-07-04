package com.onenetwork.backchain.client.hyp;

import com.onenetwork.backchain.client.BackchainClientConfig;

/**
 * Implementation of {@link BackchainClientConfig} for an
 * <a href="https://https://www.hyperledger.org/projects/fabric/">Hyperledger fabric</a>-based Backchain.
 */
public class HyperledgerConfig implements BackchainClientConfig {

	private String url;
	private String privateKey;

	/**
	 * Returns the HTTP URL of the Hyperledger fabric backchain
	 * @return HTTP URL of the Hyperledger fabric backchain
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the HTTP URL of the Hyperledger fabric backchain
	 * @param url HTTP URL of the Hyperledger fabric backchain
	 * @return this config object
	 */
	public HyperledgerConfig setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * Returns the private key of the Hyperledger fabric client, as a base64-encoded string
	 * starting with "0x".  For example, "0x8ad0132f808d0830c533d7673cd689b7fde2d349ff0610e5c04ceb9d6efb4eb1"
	 * @return private key of the Hyperledger fabric client, as a base64-encoded string starting with "0x"
	 */
	public String getPrivateKey() {
		return privateKey;
	}

	/**
	 * Sets the private key of the Hyperledger fabric client, as a base64-encoded string
	 * starting with "0x".  For example, "0x8ad0132f808d0830c533d7673cd689b7fde2d349ff0610e5c04ceb9d6efb4eb1"
	 * Needed only if you intend to post to the backchain.  If you are only verifying, you can skip this.
	 * @param privateKey private key of the Hyperledger fabric client, as a base64-encoded string starting with "0x"
	 * @return this config object
	 */
	public HyperledgerConfig setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
		return this;
	}

}
