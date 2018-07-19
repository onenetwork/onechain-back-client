var oneChain = require('../backchain-client')
var expect = require('chai').expect;
var sha256 = require('js-sha256').sha256;

function newHash() {
    return "0x" + sha256('' + new Date().getTime() + ' ' + Math.random());
}
var sampleHash = newHash();

var obc = oneChain.createContentBcClient({
    blockchain: 'hyp',
    url: 'http://localhost:4000',
    token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MzIwMjY4NTEsInVzZXJuYW1lIjoiT3JjaGVzdHJhdG9yVXNlciIsIm9yZ05hbWUiOiJPcmNoZXN0cmF0b3JPcmciLCJpYXQiOjE1MzE5OTA4NTF9.Ioa1Qb0bGrmLBzl16Ncgu7rb9N3pe7r-p3IBOYbKYa0'
});

var pbc = oneChain.createContentBcClient({
    blockchain: 'hyp',
    url: 'http://localhost:4000',
    token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MzIwMjY4NTcsInVzZXJuYW1lIjoiUGFydGljaXBhbnRVc2VyIiwib3JnTmFtZSI6IlBhcnRpY2lwYW50T3JnIiwiaWF0IjoxNTMxOTkwODU3fQ.pz-Wv_94sv1XrZtScUtWbFyhDcW4enwNTKm2K7VnZgo'
});

describe('contentbackchain-client', function() {
    
        describe('HyperLedger Fabric', function() {
    
            it('supports all smart contract methods for ContentBackchain', function() {
                var initialHashCount;
                obc.hashCount().then((hashCount) => {
                    initialHashCount = hashCount;
                    return obc.post(sampleHash);
                }).then(() => {
                    return obc.verify(sampleHash);
                }).then((verified) => {
                    expect(verified).to.be.true;
                    return obc.hashCount();
                }).then((hashCount) => {
                    expect(hashCount).to.equal(initialHashCount + 1);
                    return obc.verify(newHash());
                }).then((verified) => {
                    expect(verified).to.be.false;
                    return obc.hashCount();
                }).then((hashCount) => {
                    return pbc.verify(sampleHash);
                }).then((verified) => {
                    expect(verified).to.be.true;
                    return pbc.hashCount();
                }).then((hashCount) => {
                    expect(hashCount).to.equal(initialHashCount + 1);
                    return pbc.verify(newHash());
                }).then((verified) => {
                    expect(verified).to.be.false;
                    console.log('ContentBackchain all test cases passed. :)');
                })
          });
    });
 
});

