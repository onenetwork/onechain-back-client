var backchainClient = require('../backchain-client')
var expect = require('chai').expect;
var sha256 = require('js-sha256').sha256;

function newHash() { return "0x" + sha256('' + new Date().getTime() + ' ' + Math.random()); }
var sampleHash = newHash();

var bc = backchainClient({ 
  blockchain: 'eth', 
  url: 'http://localhost:8545', 
  contractAddress: "0xc5d4b021858a17828532e484b915149af5e1b138",
  privateKey: "0x8ad0132f808d0830c533d7673cd689b7fde2d349ff0610e5c04ceb9d6efb4eb1"
});

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
