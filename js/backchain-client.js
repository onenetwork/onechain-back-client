'use strict';

var eth = require('./eth/eth-backchain-client-impl');
var hyp = require('./hyp/hyp-backchain-client-impl');

module.exports = {
    /**
     * Constructs a new client
     * @param {url} HTTP URL of the blockchain (e.g. http://localhost:8545)
     */
    createContentBcClient: function(config) {
        if (config.blockchain == 'eth') {
            return eth.createContentBcClient(config);
        } else if(config.blockchain == 'hyp'){
            return hyp.createContentBcClient(config);
        } else{
            throw new Error('blockchain not supported: ' + config.blockchain);
        }

    },

    createDisputeBcClient: function(config) {
        if (config.blockchain == 'eth') {
            return eth.createDisputeBcClient(config);
        } else{
            throw new Error('blockchain not supported: ' + config.blockchain);
        }
    }
}
