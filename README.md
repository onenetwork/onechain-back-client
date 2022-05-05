# onechain-back-client

Provides the client API for One Network Enterprises' Backchain.  Once constructed, the 
client API frees the client as much as possible from the specifics of the Backchain's 
underlying blockchain implementation (e.g. [Etherium](https://www.ethereum.org/)
or [Hyperledger Fabric](https://www.hyperledger.org/projects/fabric)).

ONE provides client APIs for:

 - [Javascript](#javascript-client)
 - [Java](#java-client)

Licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

## Javascript Client

### Installation

The javascript client is available as a public npm module.  You can install it using the following command:

```
npm install -P @onenetwork/one-backchain-client --no-bin-links
```


### Sample Usage

```javascript
oneChain = require('@onenetwork/one-backchain-client');

// Instantiate the backchain client for an Etherium-based network
var contentBC = oneChain.createContentBcClient({
  blockchain: 'eth',  
  url: 'http://192.168.201.55:8545', 
  contentBackchainContractAddress: "0xc5d4b021858a17828532e484b915149af5e1b138",
  disputeBackchainContractAddress: "0x4a6886a515a4b800f4591a6d6a60e6004a3645ab"
});

// OR

// Instantiate the backchain client for an Hyperledger-Fabric-based network
var contentBC = oneChain.createContentBcClient({
  blockchain: 'hyp',  
  url: 'http://192.168.201.55:4000', 
  token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MzAyMTM4MzksInVzZXJuYW1lIjoiT3JjaGVzdHJhdG9yVXNlciIsIm9yZ05hbWUiOiJPcmNoZXN0cmF0b3JPcmciLCJpYXQiOjE1MzAxNzc4Mzl9.h5ARvYV4jLMMQpFJNBvinaU1tD1MkKWiengYzzOG1w8'
});



/**
 * Invoke the hashCount API to return the total number of hashes stored
 * on the Backchain
 */
contentBC.hashCount().then(function(hashCount) {
  console.info("Backchain hashCount : " + hashCount);
});

/**
 * Invoke the verify function to see if a particular hash value
 * is stored on the Backchain.
 */
var sampleHash = '0xa1effcdcb5a879f222bbb028ff5f0e9571cd992600d61b2c56e7ba24c75548c3';
contentBC.verify(sampleHash).then(function(verified) {
  console.info(sampleHash + " is on the Backchain? " + verified);
});
```

Sample output:
```
Backchain hashCount is: 13
0xa1effcdcb5a879f222bbb028ff5f0e9571cd992600d61b2c56e7ba24c75548c3 is on the Backchain? true
```


### API

#### Creation

A call to `require('@onenetwork/one-backchain-client')` returns a factory function which returns
a new client when called.  It expects a single parameter of the following form:

```javascript
// Etherium Netowrk   
{
  // blockchain type Etherium
  blockchain: 'eth',   
  
  // http(s) url of a node in the blockchain.
  url: 'http://localhost:8545', 
  
  // Address to which the Backchain "Content" contract has been bound
  contentBackchainContractAddress: "0xc5d4b021858a17828532e484b915149af5e1b138",

  // Address to which the Backchain "Dispute" contract has been bound
  disputeBackchainContractAddress: "0x4a6886a515a4b800f4591a6d6a60e6004a3645ab"
}

// OR

// Hyperledger Netowrk   
{
  // blockchain type Hyperledger fabric 
  blockchain: 'hyp',   
  
  // http(s) url of a node in the blockchain.
  url: 'http://localhost:4000', 
  
  // JWT access token for Orchestrator or Participant 
  token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MzAyMTM4MzksInVzZXJuYW1lIjoiT3JjaGVzdHJhdG9yVXNlciIsIm9yZ05hbWUiOiJPcmNoZXN0cmF0b3JPcmciLCJpYXQiOjE1MzAxNzc4Mzl9.h5ARvYV4jLMMQpFJNBvinaU1tD1MkKWiengYzzOG1w8'
}
```

#### Methods

The client object returned by the factory function supports the following methods:

| Method | Description |
| --- | --- |
| getHash(index) | Hashes are stored to the Backchain sequentially in insertion order.   This method returns a promise which returns the hash stored at the given index (starting from zero). Not support for Hyperledger fabric network. |
| getOrchestrator() | Returns a promise which returns the address of entity acting as the Orchestrator for the Backchain. |
| hashCount() | Returns a promise which returns the total number of hashes stored in the backchain.  See also `getHash(index)` |
| post(hash) | Posts a new hash to the Backchain.  Available only to the Orchestrator.  Returns a promise with no arguments. |
| verify(hash) | Returns a promise which returns true if the given hash is present in the Backchain, false otherwise. |


#### Properties

The client object returned by the factory function supports the following properties:

| Method | Description |
| --- | --- |
| config | Captures the user's initial configuration, including `blockchain`, `url`. Network specific config such as `contractAddress` of etherium or `token` in case of hyperledger fabric network |


## Java Client

### Installation

The java client is available as a maven dependency from ONE's bintray repo: <a href="https://dl.bintray.com/onenetwork/onechain">https://dl.bintray.com/onenetwork/onechain</a>

```
<dependency>
  <groupId>com.onenetwork.onechain</groupId>
  <artifactId>onechain-back-client</artifactId>
  <version>0.4.0</version>
</dependency>
```

### Sample Usage

```java
// Client for Ethereum network
EthereumConfig cfg = new EthereumConfig()
  .setUrl("http://192.168.201.55:8545")
  .setContentBackchainContractAddress("0xc5d4b021858a17828532e484b915149af5e1b138")
  .setDisputeBackchainContractAddress("0x4a6886a515a4b800f4591a6d6a60e6004a3645ab")
  .setPrivateKey("0x8ad0132f808d0830c533d7673cd689b7fde2d349ff0610e5c04ceb9d6efb4eb1")
  .setGasPrice(BigInteger.valueOf(0L)).setGasLimit(BigInteger.valueOf(999999L));

// OR

// Client for Hyperledger fabric network
HyperledgerConfig cfg = new HyperledgerConfig()
  .setUrl("http://192.168.201.55:8545")
  .setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MzAyMTM4MzksInVzZXJuYW1lIjoiT3JjaGVzdHJhdG9yVXNlciIsIm9yZ05hbWUiOiJPcmNoZXN0cmF0b3JPcmciLCJpYXQiOjE1MzAxNzc4Mzl9.h5ARvYV4jLMMQpFJNBvinaU1tD1MkKWiengYzzOG1w8");

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

### Building

To build the java client, run the following (using JDK 1.8):

```
gradlew clean jar
```

To generate maven artifacts for publication, run:

```
gradlew publishToMavenLocal
```

This will generate a POM file into java\build\publications\mavenJava
