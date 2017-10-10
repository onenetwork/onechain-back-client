var backchainClient = require('../backchain-client')
var expect = require('chai').expect;

var bc = backchainClient({ 
  blockchain: 'eth', 
  url: 'http://localhost:8545', 
  contractAddress: "0x7da7b16f366f14adeb589f628c9e7232c6dfb0e2",
  fromAddress: "0xb9afc7def72f3025892da2912348b3e877f36f94"
});

describe('backchain-client', function() {
  describe('etherium', function() {
    it('supports all smart contract methods', function() {
      bc.post("0xafef74575dfb567cd95678f80c8c2681d2c084da2a95b3643cf6e13e739f4480").then(function(result) {
        return bc.verify("0xafef74575dfb567cd95678f80c8c2681d2c084da2a95b3643cf6e13e739f4480");
      }).then(function(verified) {
        expect(verified).to.equal(true);
        return bc.verify("0xbfef74575dfb567cd95678f80c8c2681d2c084da2a95b3643cf6e13e739f4480");
      }).then(function(verified) {
        expect(verified).to.equal(false);
        return bc.hashCount();
      }).then(function(result) {
        expect(result).to.above(0);
        return bc.getHash(0);
      }).then(function(hashAtZero) {
        return bc.getOrchestrator();
      }).then(function(orchestrator) {
        expect(orchestrator.toUpperCase()).to.equal(bc.config.fromAddress.toUpperCase());
      });
    });
  });
});