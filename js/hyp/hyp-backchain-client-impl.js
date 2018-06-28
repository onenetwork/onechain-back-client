'use strict';

const request = require('request-promise')

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
                    'authorization': 'Bearer '+config.privateKey
                }
            };

            var params = {fcn: fcn, args: args};
            if('post' === fcn){
                options['uri'] = baseUri + 'invoke';
                options['method'] = 'POST';
                options['body'] = params;
            }else{
                options['uri'] = baseUri + 'query';
                options['method'] = 'GET';
                options['qs'] = params;
            }
            
            return request(options);
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
            }            
        };
    },
 
}
