var oneChain = require('../backchain-client')
var expect = require('chai').expect;
var sha256 = require('js-sha256').sha256;

function newHash() {
    return "0x" + sha256('' + new Date().getTime() + ' ' + Math.random());
}
var sampleHash = newHash();
var sampleHash1 = newHash();
var sampleHashString = sampleHash1.substring(2);

var config = {
    blockchain: 'eth',
    url: 'http://localhost:8545',
    contentBackchainContractAddress: "0xc5d4b021858a17828532e484b915149af5e1b138",
    disputeBackchainContractAddress: "0x4a6886a515a4b800f4591a6d6a60e6004a3645ab",
    privateKey: "0x8ad0132f808d0830c533d7673cd689b7fde2d349ff0610e5c04ceb9d6efb4eb1"
};

var bc = oneChain.createContentBcClient(config);

var disputeBC = oneChain.createDisputeBcClient(config);

describe('contentbackchain-client', function() {

    describe('etherium', function() {

        it('supports all smart contract methods for ContentBackchain', function() {
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
                console.log('ContentBackchain all test case passed');
            });
        });

    });

});

describe('disputeBackchain-client', function() {
    describe('etherium', function() {
        it('supports all smart dispute methods for DisputeBackchain contract', function() {
            var initialHashCount;
            disputeBC.getOrchestrator().then(function(orchestrator) {
                expect(orchestrator.toUpperCase()).to.equal(disputeBC.config.fromAddress.toUpperCase());
                var dispute = {
                    disputeId: sampleHashString,
                    disputingParty: disputeBC.config.fromAddress,
                    disputedTransactionId: "0xc5d4b021858a17828532e484b915149af5e1b138",
                    reason: "TRANSACTION_PARTIES_DISPUTED"
                };
                return disputeBC.submitDispute(dispute);
            }).then(function() {
                disputeFilter = {};
                return disputeBC.filterDisputes(disputeFilter);
            }).then(function(disputes) {
                var isFound = false;
                for (var i = 0; i < disputes.length; i++) {
                    if (disputes[i].disputeId === sampleHash1) {
                        isFound = true;
                        break;
                    }
                }
                expect(isFound).to.be.true;
                return disputeBC.closeDispute(sampleHashString);
            }).then(function() {
                return disputeBC.getDispute(sampleHashString);
            }).then(function(dispute) {
                expect(dispute.state).to.equal("CLOSED");
                var dispute = {
                    disputedTransactionId: "0xc5d4b021858a17828532e484b915149af5e1b133",
                    reason: "FINANCIAL_DISPUTED"
                };
                return disputeBC.submitDispute(dispute);
            }).then(function() {
                var disputeFilter = {
                    reason: ["FINANCIAL_DISPUTED"]
                };
                return disputeBC.filterDisputes(disputeFilter);
            }).then(function(disputes) {
                var disputeFilter = {
                    disputeId: null
                };
                return disputeBC.getDisputeCount(disputeFilter);
            }).then(function(count) {
                expect(count).to.equal(1);
                var dispute = {
                    disputedTransactionId: "0xc5d4b021858a17828532e484b915149af5e1b133",
                    reason: "FINANCIAL_DISPUTED",
                    disputingParty: '0x0000000000000000001'
                };
                return disputeBC.submitDispute(dispute);
            }).then(function() {
                return Promise.reject("Exception anticipated");
            }).catch(function(reason) {
                if ((reason.message || reason).indexOf("DisputingParty") > -1) {
                    assert.fail(reason.message);
                }
                console.log('DisputeBackchain all test case passed');
            });
        });
    });
});