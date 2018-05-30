# onechain-back-client

Provides the client API for One Network Enterprises' Backchain.  Once constructed, the 
client API frees the client as much as possible from the specifics of the Backchain's 
underlying blockchain implementation (e.g. [Etherium](https://www.ethereum.org/)
or [Hyperledger Fabric](https://www.hyperledger.org/projects/fabric)).

ONE provides client APIs for:

 - [Javascript](#javascript-client)
 - [Java](#java-client)

<br/>
Licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
<br/>
<br/>

## Javascript Client

### Installation

The javascript client is available as a public npm module.  You can install it using the following command:

```
npm install -P @onenetwork/one-backchain-client --no-bin-links
```


### Sample Usage: Content Backchain

```javascript
oneBcClient = require('@onenetwork/one-backchain-client');

/**
 * Instantiate the content backchain client,
 * providing real values for url and contractAddress
 */
var contentBackchain = oneBcClient.createContentBcClient({ 
  blockchain: 'eth', 
  url: 'http://192.168.201.55:8545', 
  contentBackchainContractAddress: "0xc5d4b021858a17828532e484b915149af5e1b138",
  privateKey: "0x8ad0132f808d0830c533d7673cd689b7fde2d349ff0610e5c04ceb9d6efb4eb1"
  gas: 10000,
  gasPrice: '0.00 USD'
});

/**
 * Invoke the hashCount API to return the total number of hashes stored
 * on the Content Backchain
 */
contentBackchain.hashCount().then(function(hashCount) {
  console.info("Backchain hashCount : " + hashCount);
});

/**
 * Invoke the verify function to see if a particular hash value
 * is stored on the Content Backchain.
 */
var sampleHash = '0xa1effcdcb5a879f222bbb028ff5f0e9571cd992600d61b2c56e7ba24c75548c3';
contentBackchain.verify(sampleHash).then(function(verified) {
  console.info(sampleHash + " is on the ContentBackchain? " + verified);
});
```

Sample output:
```
ContentBackchain hashCount is: 13
0xa1effcdcb5a879f222bbb028ff5f0e9571cd992600d61b2c56e7ba24c75548c3 is on the ContentBackchain? true
```
### Sample Usage: Dispute Backchain

```javascript
oneBcClient = require('@onenetwork/one-backchain-client');

/**
 * Instantiate the content backchain client,
 * providing real values for url and contractAddress
 */
var disputeBackchain = oneBcClient.createDisputeBcClient({ 
  blockchain: 'eth', 
  url: 'http://192.168.201.55:8545', 
  disputeBackchainContractAddress: "0x4a6886a515a4b800f4591a6d6a60e6004a3645ab",
  privateKey: "0x8ad0132f808d0830c533d7673cd689b7fde2d349ff0610e5c04ceb9d6efb4eb1"
  gas: 10000,
  gasPrice: '0.00 USD'
});

/**
 * Invoke the hashCount API to return the total number of hashes stored
 * on the Content Backchain
 */
disputeBackchain.submitDispute().then(function({}) {
  console.info("Backchain hashCount : " + hashCount);
});

/**
 * Invoke the verify function to see if a particular hash value
 * is stored on the Content Backchain.
 */
var sampleHash = '0xa1effcdcb5a879f222bbb028ff5f0e9571cd992600d61b2c56e7ba24c75548c3';
contentBackchain.verify(sampleHash).then(function(verified) {
  console.info(sampleHash + " is on the ContentBackchain? " + verified);
});
```

Sample output:
```
ContentBackchain hashCount is: 13
0xa1effcdcb5a879f222bbb028ff5f0e9571cd992600d61b2c56e7ba24c75548c3 is on the ContentBackchain? true
```


### API

#### Creation

A call to `require('@onenetwork/one-backchain-client')` returns a factory function which returns
a new client when called.  It expects a single parameter of the following form:

```javascript
{
  // blockchain type - only value supported today is 'eth' for Etherium
  blockchain: 'eth',   
  
  // http(s) url of a node in the blockchain.
  url: 'http://localhost:8545', 

  // when using eth as blockchain, provide the address to which the Contract Backchain etherium contract has been bound in the Ethereum blockchain
  contentBackchainContractAddress: "0xdd556330eb32c9daa558ab2327f7a044d292b1a2",
  
  // when using eth as blockchain, provide the address to which the Dipsute Backchain etherium contract has been bound in the Ethereum blockchain
  disputeBackchainContractAddress: "0x4a6886a515a4b800f4591a6d6a60e6004a3645ab",
  
  //The maximum gas provided for a transaction (gas limit).
  gas: 10000,
  
  //The gas price in wei to use for transactions.
  gasPrice: '0.00'
}
```

#### Methods

The client object returned by the factory function supports the following methods:

| Method | Description |
| --- | --- |
| getHash(index) | Hashes are stored to the Backchain sequentially in insertion order.   This method returns a promise which returns the hash stored at the given index (starting from zero). |
| getOrchestrator() | Returns a promise which returns the address of entity acting as the Orchestrator for the Backchain. |
| hashCount() | Returns a promise which returns the total number of hashes stored in the backchain.  See also `getHash(index)` |
| post(hash) | Posts a new hash to the Backchain.  Available only to the Orchestrator.  Returns a promise with no arguments. |
| verify(hash) | Returns a promise which returns true if the given hash is present in the Backchain, false otherwise. |


#### Properties

The client object returned by the factory function supports the following properties:

| Method | Description |
| --- | --- |
| config | Captures the user's initial configuration, including `blockchain`, `url` and `contractAddress` |


## Java Client

### Installation

The java client is available as a maven dependency from ONE's bintray repo: <a href="https://dl.bintray.com/onenetwork/onechain">https://dl.bintray.com/onenetwork/onechain</a>

```
<dependency>
  <groupId>com.onenetwork.onechain</groupId>
  <artifactId>onechain-back-client</artifactId>
  <version>0.2.0</version>
</dependency>
```

### Sample Usage

```java
EthereumConfig cfg = new EthereumConfig()
  .setUrl("http://192.168.201.55:8545")
  .setContentBackchainContractAddress("0xc5d4b021858a17828532e484b915149af5e1b138")
  .setDisputeBackchainContractAddress("0x4a6886a515a4b800f4591a6d6a60e6004a3645ab")
  .setPrivateKey("0x8ad0132f808d0830c533d7673cd689b7fde2d349ff0610e5c04ceb9d6efb4eb1")
  .setGasPrice(BigInteger.valueOf(0L)).setGasLimit(BigInteger.valueOf(999999L));

ContentBackchainClient cbk = BackchainClientFactory.newContentBackchainClient(cfg);
System.out.println("Backchain hashCount : " + cbk.hashCount());
String sampleHash = "0xa1effcdcb5a879f222bbb028ff5f0e9571cd992600d61b2c56e7ba24c75548c3";
System.out.println(sampleHash + " is on the Backchain? " + cbk.verify(sampleHash));

DisputeBackchainClient dbk = BackchainClientFactory.newDisputeBackchainClient(cfg);
Dispute dispute = new Dispute()
  .setDisputedTransactionID("0x90aa7ef75567ce9b2f3de15f9b4825125e393fbbfa0aa65d8e5d51b218900273")
  .setReason(Dispute.Reason.HASH_NOT_FOUND);
dbk.submitDispute(dispute);
System.out.println(dbk.getDispute(dispute.getDisputeID()).getState());
```

Sample output:
```
Backchain hashCount is: 13
0xa1effcdcb5a879f222bbb028ff5f0e9571cd992600d61b2c56e7ba24c75548c3 is on the Backchain? true
OPEN
```


### API

Please view the <a href="https://onenetwork.github.io/onechain-back-client/javadoc/">ONE Backchain Client Javadocs</a>
