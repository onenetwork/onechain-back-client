package com.onenetwork.backchain.client.eth;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;

import com.onenetwork.backchain.client.BackchainClient;
import com.onenetwork.backchain.client.BackchainClientConfig;

/**
 * Implementation of {@link BackchainClient} for an
 * <a href="https://www.ethereum.org/">Ethereum</a>-based Backchain.
 */
public class EthereumClient implements BackchainClient {

	private Web3j web3j;
	private BackchainABI backchainABI;

	/**
	 * @param config Ethereum-based {@link BackchainClientConfig}
	 */
	public EthereumClient(EthereumConfig config) {
		web3j = Web3j.build(new HttpService(config.getUrl()));
		if (config.getPrivateKey() != null) {
			Credentials credentials = Credentials.create(config.getPrivateKey());
			backchainABI = BackchainABI.load(config.getContractAddress(), web3j, credentials, config.getGasPrice(),
					config.getGasLimit());
		}
		else {
			ClientTransactionManager tm = new ClientTransactionManager(web3j, "0x00000000000000000000000000000000");
			backchainABI = BackchainABI.load(config.getContractAddress(), web3j, tm, config.getGasPrice(),
					config.getGasLimit());
		}
	}

	interface Supplier<T> {
		T get() throws Exception;
	}

	private <T> T await(Supplier<T> s) {
		try {
			return s.get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Bytes32 hashStringToBytes(String hash) throws DecoderException {
		if (!hash.toUpperCase().startsWith("0X")) {
			throw new IllegalArgumentException("Hash " + hash + " must begin with 0x");
		}
		return new Bytes32(Hex.decodeHex(hash.substring(2, hash.length()).toCharArray()));
	}

	private String hashBytesToString(Bytes32 bytes) throws DecoderException {
		return bytes == null ? null : "0x" + new String(Hex.encodeHex(bytes.getValue()));
	}

	@Override
	public String getHash(long index) {
		return await(() -> hashBytesToString(backchainABI.getHash(new Uint256(index)).get()));
	}

	@Override
	public String getOrchestrator() {
		return await(() -> backchainABI.orchestrator().get().toString());
	}

	@Override
	public long hashCount() {
		return await(() -> backchainABI.hashCount().get().getValue().longValue());
	}

	@Override
	public void post(String hash) {
		await(() -> backchainABI.post(hashStringToBytes(hash)).get());
	}

	@Override
	public boolean verify(String hash) {
		return await(() -> backchainABI.verify(hashStringToBytes(hash)).get().getValue());
	}

}
