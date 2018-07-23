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
    token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Ik9yY2hlc3RyYXRvclVzZXIiLCJvcmdOYW1lIjoiT3JjaGVzdHJhdG9yT3JnIiwiaWF0IjoxNTMyMDAxMzgzfQ.Wy8RITjRfd3O5wvvoUr7ReIMHtsdpYO6nB385vXIwTU'
});

var pbc = oneChain.createContentBcClient({
    blockchain: 'hyp',
    url: 'http://localhost:4000',
    token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlBhcnRpY2lwYW50VXNlciIsIm9yZ05hbWUiOiJQYXJ0aWNpcGFudE9yZyIsImlhdCI6MTUzMjAwMTM5MH0.Jw3bgaoOHKs6pJmsTF97mJRnoq3GCyyDW0QbLeSBAfU'
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
                    return pbc.getOrchestrator();
                }).then(function(orchestrator) {
                    expect(orchestrator).to.equal("0x2d2d2d2d2d424547494e205055424c4943204b45592d2d2d2d2d0d0a4d466b77457759484b6f5a497a6a3043415159494b6f5a497a6a3044415163445167414536486f474a3564616c456a47417950476777654c674c365054326c4c0d0a635675344c6272734e316d62675378457658344d78493371647467545164493845305253734949746a696e74714c2b6f562f6f574b78383836413d3d0d0a2d2d2d2d2d454e44205055424c4943204b45592d2d2d2d2d0d0a");
                    console.log('ContentBackchain all test case passed :)');
                });
          });
    });
 
});

