'use strict';

var fs = require('fs');
var Web3 = require('Web3');

/**
 * Constructs a new client
 * @param {url} HTTP URL of the blockchain (e.g. http://localhost:8545)
 */
module.exports = function(web3) {
  var abi = JSON.parse(fs.readFileSync('BackchainABI.json').toString());
  var contract = new web3.eth.Contract(abi, "0x7da7b16f366f14adeb589f628c9e7232c6dfb0e2", {
    from: "0xb9afc7def72f3025892da2912348b3e877f36f94"
  });
  return {
    hashCount: function() {
      return contract.methods.hashCount().call();
    },
    post: function(hash) {
      return contract.methods.post(hash).send();
    },
    verify: function(hash) {
      return contract.methods.verify(hash).call();
    },
    getHash: function(index) {
      return contract.methods.getHash(index).call();
    },
    getOrchestrator: function() {
      return contract.methods.orchestrator().call();
    }
  };
};

var bc = new module.exports(new Web3(new Web3.providers.HttpProvider("http://localhost:8545")));

bc.hashCount().then(function(result) {
  console.info(result);
  return bc.post("0xafef74575dfb567cd95678f80c8c2681d2c084da2a95b3643cf6e13e739f4480");
}).then(function() {
  console.info("post done");
  return bc.verify("0xafef74575dfb567cd95678f80c8c2681d2c084da2a95b3643cf6e13e739f4480");
}).then(function(verified) {
  console.info("verified add? ", verified);
  return bc.verify("0xbfef74575dfb567cd95678f80c8c2681d2c084da2a95b3643cf6e13e739f4480");
}).then(function(verified) {
  console.info("verified no-add? ", verified);
  return bc.hashCount();
}).then(function(result) {
  console.info("new count", result);
  return bc.getHash(0);
}).then(function(hashAtZero) {
  console.info("hashAtZero", hashAtZero);
  return bc.getOrchestrator();
}).then(function(orchestrator) {
  console.info("orchestrator", orchestrator);
});
