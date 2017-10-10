var backchainClient = require('../backchain-client')
var expect = require('chai').expect;
var sha256 = require('js-sha256').sha256;

var bc = backchainClient({ 
  blockchain: 'eth', 
  url: 'http://localhost:8545', 
  contractAddress: "0x7da7b16f366f14adeb589f628c9e7232c6dfb0e2",
  fromAddress: "0xb9afc7def72f3025892da2912348b3e877f36f94"
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