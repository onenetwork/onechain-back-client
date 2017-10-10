'use strict';

var fs = require('fs');
var Web3 = require('Web3');

/**
 * Constructs a new client
 * @param {url} HTTP URL of the blockchain (e.g. http://localhost:8545)
 */
module.exports = function(config) {
  if (config.blockchain != 'eth') {
    throw new Error('blockchain not supported: ' + config.blockchain);
  }
  
  var web3 = new Web3(new Web3.providers.HttpProvider(config.url));
  
  // Warning - this abi must be updated any time Backchain.sol changes
  var abi = [{"type":"function","payable":false,"outputs":[{"type":"uint256","name":""}],"name":"hashCount","inputs":[],"constant":true},{"type":"function","payable":false,"outputs":[{"type":"bytes32","name":""}],"name":"getHash","inputs":[{"type":"uint256","name":"index"}],"constant":true},{"type":"function","payable":false,"outputs":[{"type":"bool","name":""}],"name":"verify","inputs":[{"type":"bytes32","name":"hash"}],"constant":true},{"type":"function","payable":false,"outputs":[],"name":"post","inputs":[{"type":"bytes32","name":"hash"}],"constant":false},{"type":"function","payable":false,"outputs":[{"type":"address","name":""}],"name":"orchestrator","inputs":[],"constant":true},{"type":"function","payable":false,"outputs":[{"type":"bool","name":""}],"name":"hashMapping","inputs":[{"type":"bytes32","name":""}],"constant":true},{"type":"constructor","payable":false,"inputs":[]}];
  
  var contract = new web3.eth.Contract(abi, config.contractAddress, {
    from: config.fromAddress
  });
  return {
    config: config,
    hashCount: function() {
      return contract.methods.hashCount().call().then(function(result) { 
        return Promise.resolve(parseInt(result)) 
      });
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
