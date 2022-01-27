'use strict';

const axios = require('axios');

module.exports = {
    /**
     * Constructs a new client
     * @param {url} HTTP URL of the blockchain (e.g. http://localhost:4000)
     */
    createContentBcClient: function(config) {
        
        // Utility to execute http request
        var executeRequest = function(fcn, args){
            var baseUri = config.url;
            if(!baseUri.endsWith("/")){
                baseUri += "/";
            }

            var options = {
                'json': true,
                headers: {
                    'content-type' : 'application/json',
                    'authorization': 'Bearer ' + config.token
                }
            };

            var params = {fcn: fcn, args: args};
            if('post' === fcn){
              return axios.post(baseUri + 'invoke', {
                params: params
              });
            }else{
              return axios.get(baseUri + 'query', {
                params: params
              });
            }
        }
        return {
            config: config,
            hashCount: function() {
                return executeRequest('hashCount', ' ').then(function(hashCount){
                    return  Promise.resolve(parseInt(hashCount));
                });
            },
            post: function(hash) {
                return executeRequest('post', hash);
            },
            verify: function(hash) {
              return executeRequest('verify', hash);
            },
            getOrchestrator: function(hash) {
                return executeRequest('getOrchestrator', ' ');
            },
            getHash: function(index) {
              throw new Error('Not supported for Hyperledger fabric implementatoion.');
            },
        };
    },
 
}
