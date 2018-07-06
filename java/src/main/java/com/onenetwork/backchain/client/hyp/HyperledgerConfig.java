package com.onenetwork.backchain.client.hyp;

import com.onenetwork.backchain.client.BackchainClientConfig;

/**
 * Implementation of {@link BackchainClientConfig} for an
 * <a href="https://https://www.hyperledger.org/projects/fabric/">Hyperledger fabric</a>-based Backchain.
 */
public class HyperledgerConfig implements BackchainClientConfig {

	private String url;
	private String token;

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
	 * Returns the token of the Hyperledger fabric client, as a JSON Web Token
	 * starting with "eyJhbGci".  For example, "0x8ad0132f808d0830c533d7673cd689b7fde2d349ff0610e5c04ceb9d6efb4eb1"
	 * @return token of the Hyperledger fabric client, as a base64-encoded string starting with "eyJhbGci"
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Sets the token of the Hyperledger fabric client, as a JSON Web Token
	 * starting with "eyJhbGci".  For example, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MzA4OTAyNzEsInVzZXJuYW1lIjoiT3JjaGVzdHJhdG9yVXNlciIsIm9yZ05hbWUiOiJPcmNoZXN0cmF0b3JPcmciLCJpYXQiOjE1MzA4NTQyNzF9.B3eDgGcgvLxUWvjbuAkjgWPQS5j4EXmKkpgjd-BbGy0"
	 * Needed only if you intend to post to the backchain.  If you are only verifying, you can skip this.
	 * @param privateKey token of the Hyperledger fabric client, as a JSON Web Token starting with "eyJhbGci"
	 * @return this config object
	 */
	public HyperledgerConfig setToken(String token) {
		this.token = token;
		return this;
	}

}
