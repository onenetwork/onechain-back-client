var backchainClient = require('../backchain-client')
var expect = require('chai').expect;
var sha256 = require('js-sha256').sha256;

var bc = backchainClient({ 
  blockchain: 'eth', 
  url: 'http://localhost:8545', 
  contractAddress: "0xc651792a7014053fcaecd7cb39533be1b84f9503",
  fromAddress: "0xf796b0a29a1e005cb71488055b2c6ff951e973b3"
});

function newHash() { return "0x" + sha256('' + new Date().getTime() + ' ' + Math.random()); }
var sampleHash = newHash();

describe('backchain-client', function() {
  describe('etherium', function() {
    it('supports all smart contract methods', function() {
      var initialHashCount;
      
      bc.hashCount().then(function(hashCount) {
        initialHashCount = hashCount;
        return bc.post(sampleHash);
      }).then(function(hashCount) {
        return bc.verify(sampleHash);
      }).then(function(verified) {
        expect(verified).to.be.true;
        return bc.hashCount();
      }).then(function(hashCount) {
        expect(hashCount).to.equal(initialHashCount + 1);
        return bc.verify(newHash());
      }).then(function(verified) {
        expect(verified).to.be.false;
        return bc.hashCount();
      }).then(function(hashCount) {
        expect(hashCount).to.equal(initialHashCount + 1);
        return bc.getHash(hashCount - 1);
      }).then(function(hash) {
        expect(hash).to.equal(sampleHash);
        return bc.getOrchestrator();
      }).then(function(orchestrator) {
        expect(orchestrator.toUpperCase()).to.equal(bc.config.fromAddress.toUpperCase());
      });
    });
  });
});