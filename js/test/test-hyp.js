var oneChain = require('../backchain-client')
var expect = require('chai').expect;
var sha256 = require('js-sha256').sha256;

function newHash() {
    return "0x" + sha256('' + new Date().getTime() + ' ' + Math.random());
}
var sampleHash = newHash();
var sampleHash1 = newHash();
var sampleHashString = sampleHash1.substring(2);

var contractConfig = {
    blockchain: 'hyp',
    url: 'http://localhost:4000',
    token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MzAyMTM4MzksInVzZXJuYW1lIjoiT3JjaGVzdHJhdG9yVXNlciIsIm9yZ05hbWUiOiJPcmNoZXN0cmF0b3JPcmciLCJpYXQiOjE1MzAxNzc4Mzl9.h5ARvYV4jLMMQpFJNBvinaU1tD1MkKWiengYzzOG1w8'
};


var bc = oneChain.createContentBcClient(contractConfig);

describe('contentbackchain-client', function() {
    
        describe('hyperledger', function() {
    
            it('supports all smart contract methods for ContentBackchain', function() {
                var initialHashCount;
                bc.hashCount().then((hashCount) => {
		            initialHashCount = hashCount;
		            return bc.post(sampleHash);
                }).then(() => {
                    return bc.verify(sampleHash);
                }).then((verified) => {
                    expect(verified).to.be.true;
                    return bc.hashCount();
                }).then((hashCount) => {
                    expect(hashCount).to.equal(initialHashCount + 1);
                    return bc.verify(newHash());
                }).then((verified) => {
                    expect(verified).to.be.false;
                    return bc.hashCount();
                }).then((hashCount) => {  
                    expect(hashCount).to.equal(initialHashCount + 1);            
                    console.log('ContentBackchain all test cases passed :)');
            	});
    	
          });
    });
});

