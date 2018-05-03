'use strict';

var fs = require('fs');
var Web3 = require('web3');

module.exports = {
    /**
     * Constructs a new client
     * @param {url} HTTP URL of the blockchain (e.g. http://localhost:8545)
     */
    createContentBcClient: function(config) {
        if (config.blockchain != 'eth') {
            throw new Error('blockchain not supported: ' + config.blockchain);
        }
        var web3;
        if (config.web3 !== undefined) {
            web3 = config.web3;
        } else {
            web3 = new Web3(new Web3.providers.HttpProvider(config.url));
        }

        // Warning - this abi must be updated any time Backchain.sol changes
        var abi = [{
            "type": "function",
            "payable": false,
            "outputs": [{
                "type": "uint256",
                "name": ""
            }],
            "name": "hashCount",
            "inputs": [],
            "constant": true
        }, {
            "type": "function",
            "payable": false,
            "outputs": [{
                "type": "bytes32",
                "name": ""
            }],
            "name": "getHash",
            "inputs": [{
                "type": "uint256",
                "name": "index"
            }],
            "constant": true
        }, {
            "type": "function",
            "payable": false,
            "outputs": [{
                "type": "bool",
                "name": ""
            }],
            "name": "verify",
            "inputs": [{
                "type": "bytes32",
                "name": "hash"
            }],
            "constant": true
        }, {
            "type": "function",
            "payable": false,
            "outputs": [],
            "name": "post",
            "inputs": [{
                "type": "bytes32",
                "name": "hash"
            }],
            "constant": false
        }, {
            "type": "function",
            "payable": false,
            "outputs": [{
                "type": "address",
                "name": ""
            }],
            "name": "orchestrator",
            "inputs": [],
            "constant": true
        }, {
            "type": "function",
            "payable": false,
            "outputs": [{
                "type": "bool",
                "name": ""
            }],
            "name": "hashMapping",
            "inputs": [{
                "type": "bytes32",
                "name": ""
            }],
            "constant": true
        }, {
            "type": "constructor",
            "payable": false,
            "inputs": []
        }];

        if (!config.fromAddress) {
            if (config.privateKey) {
                config.fromAddress = web3.eth.accounts.privateKeyToAccount(config.privateKey).address;
            } else {
                //read-only access
                config.fromAddress = '0x0000000000000000000000000000000000000000';
            }
        }
        var contract = new web3.eth.Contract(abi, config.contractAddress, {
            from: config.fromAddress
        });
        return {
            config: config,
            hashCount: function() {
                return contract.methods.hashCount().call().then(function(result) {
                    return Promise.resolve(parseInt(result))
                });
            },
            post: function(hash) {
                return contract.methods.post(hash).send();
            },
            verify: function(hash) {
                return contract.methods.verify(hash).call();
            },
            getHash: function(index) {
                return contract.methods.getHash(index).call();
            },
            getOrchestrator: function() {
                return contract.methods.orchestrator().call();
            }
        };
    },
    createDisputeBcClients: function(config) {
        if (config.blockchain != 'eth') {
            throw new Error('blockchain not supported: ' + config.blockchain);
        }

        var web3;
        if (config.web3 !== undefined) {
            web3 = config.web3;
        } else {
            web3 = new Web3(new Web3.providers.HttpProvider(config.url));
        }

        // Warning - this abi must be updated any time DisputeBackchain.sol changes
        var abi = [{
            "constant": true,
            "inputs": [{
                "name": "id",
                "type": "bytes32"
            }],
            "name": "getDisputeHeader",
            "outputs": [{
                "name": "",
                "type": "address"
            }, {
                "name": "",
                "type": "bytes32"
            }, {
                "name": "",
                "type": "bytes32[]"
            }],
            "payable": false,
            "type": "function"
        }, {
            "constant": true,
            "inputs": [{
                "name": "id",
                "type": "bytes32"
            }],
            "name": "getDisputeDetail",
            "outputs": [{
                "name": "",
                "type": "uint256"
            }, {
                "name": "",
                "type": "uint256"
            }, {
                "name": "",
                "type": "string"
            }, {
                "name": "",
                "type": "string"
            }],
            "payable": false,
            "type": "function"
        }, {
            "constant": false,
            "inputs": [{
                "name": "id",
                "type": "bytes32"
            }],
            "name": "closeDispute",
            "outputs": [],
            "payable": false,
            "type": "function"
        }, {
            "constant": true,
            "inputs": [{
                "name": "ids",
                "type": "bytes32[]"
            }, {
                "name": "reasonValues",
                "type": "uint256[]"
            }],
            "name": "filterDisputesByReason",
            "outputs": [{
                "name": "",
                "type": "bytes32[]"
            }],
            "payable": false,
            "type": "function"
        }, {
            "constant": true,
            "inputs": [{
                "name": "ids",
                "type": "bytes32[]"
            }, {
                "name": "submittedDateStart",
                "type": "uint256"
            }, {
                "name": "submittedDateEnd",
                "type": "uint256"
            }, {
                "name": "closedDateStart",
                "type": "uint256"
            }, {
                "name": "closedDateEnd",
                "type": "uint256"
            }],
            "name": "filterDisputesByDates",
            "outputs": [{
                "name": "",
                "type": "bytes32[]"
            }],
            "payable": false,
            "type": "function"
        }, {
            "constant": false,
            "inputs": [{
                "name": "disputeID",
                "type": "bytes32"
            }, {
                "name": "disputingPartyAddress",
                "type": "address"
            }, {
                "name": "disputedTransactionID",
                "type": "bytes32"
            }, {
                "name": "disputedBusinessTransactionIDs",
                "type": "bytes32[]"
            }, {
                "name": "reasonCode",
                "type": "string"
            }],
            "name": "submitDispute",
            "outputs": [],
            "payable": false,
            "type": "function"
        }, {
            "constant": true,
            "inputs": [{
                "name": "ids",
                "type": "bytes32[]"
            }, {
                "name": "stateValues",
                "type": "uint256[]"
            }],
            "name": "filterDisputesByState",
            "outputs": [{
                "name": "",
                "type": "bytes32[]"
            }],
            "payable": false,
            "type": "function"
        }, {
            "constant": true,
            "inputs": [],
            "name": "getOrchestrator",
            "outputs": [{
                "name": "",
                "type": "address"
            }],
            "payable": false,
            "type": "function"
        }, {
            "constant": true,
            "inputs": [{
                "name": "ids",
                "type": "bytes32[]"
            }, {
                "name": "disputedBusinessTransactionIds",
                "type": "bytes32[]"
            }],
            "name": "filterDisputesByDisputedBusinessTransactionIDs",
            "outputs": [{
                "name": "",
                "type": "bytes32[]"
            }],
            "payable": false,
            "type": "function"
        }, {
            "constant": false,
            "inputs": [{
                "name": "valInMinute",
                "type": "uint256"
            }],
            "name": "setDisputeSubmissionWindowInMinutes",
            "outputs": [],
            "payable": false,
            "type": "function"
        }, {
            "constant": true,
            "inputs": [{
                "name": "ids",
                "type": "bytes32[]"
            }, {
                "name": "submittedDateStart",
                "type": "uint256"
            }, {
                "name": "submittedDateEnd",
                "type": "uint256"
            }, {
                "name": "closedDateStart",
                "type": "uint256"
            }, {
                "name": "closedDateEnd",
                "type": "uint256"
            }, {
                "name": "stateValues",
                "type": "uint256[]"
            }, {
                "name": "reasonValues",
                "type": "uint256[]"
            }],
            "name": "filterDisputeByDetail",
            "outputs": [{
                "name": "",
                "type": "bytes32[]"
            }],
            "payable": false,
            "type": "function"
        }, {
            "constant": true,
            "inputs": [],
            "name": "getDisputeSubmissionWindowInMinutes",
            "outputs": [{
                "name": "",
                "type": "uint256"
            }],
            "payable": false,
            "type": "function"
        }, {
            "constant": true,
            "inputs": [{
                "name": "ids",
                "type": "bytes32[]"
            }, {
                "name": "disputingParties",
                "type": "address[]"
            }, {
                "name": "disputedTransactionIDs",
                "type": "bytes32[]"
            }, {
                "name": "disputedBusinessTransactionIDs",
                "type": "bytes32[]"
            }],
            "name": "filterDisputeByHeaders",
            "outputs": [{
                "name": "",
                "type": "bytes32[]"
            }],
            "payable": false,
            "type": "function"
        }, {
            "constant": true,
            "inputs": [{
                "name": "ids",
                "type": "bytes32[]"
            }, {
                "name": "disputingParties",
                "type": "address[]"
            }],
            "name": "filterDisputesByDisputingParty",
            "outputs": [{
                "name": "",
                "type": "bytes32[]"
            }],
            "payable": false,
            "type": "function"
        }, {
            "constant": true,
            "inputs": [{
                "name": "ids",
                "type": "bytes32[]"
            }, {
                "name": "disputedTransactionIDs",
                "type": "bytes32[]"
            }],
            "name": "filterDisputesByDisputedTransactionIDs",
            "outputs": [{
                "name": "",
                "type": "bytes32[]"
            }],
            "payable": false,
            "type": "function"
        }, {
            "inputs": [],
            "payable": false,
            "type": "constructor"
        }];

        if (!config.fromAddress) {
            if (config.privateKey) {
                config.fromAddress = web3.eth.accounts.privateKeyToAccount(config.privateKey).address;
            } else {
                //read-only access
                config.fromAddress = '0x0000000000000000000000000000000000000000';
            }
        }
        var contentContract = new web3.eth.Contract(abi, config.contentBackchainContractAddress, {
            from: config.fromAddress
        });
        var disputeContract = new web3.eth.Contract(abi, config.disputeBackchainContractAddress, {
            from: config.fromAddress
        });
        return {
            config: config,
            submitDispute: function(dispute) {
                return disputeContract.methods.submitDispute(dispute.disputeID, dispute.disputeParty, dispute.disputedTransactionID, dispute.disputedBusinessTransactionIDs, dispute.reason).send();
            },
            filterDisputes: function(disputeFilter) {
                var arrayOfDisputes = [];
                disputeContract.methods.filterDisputeByHeaders(disputeFilter.ids, disputeFilter.disputePartys, disputeFilter.disputedTransactionIDs, disputeFilter.disputedBusinessTransactionIDs).call().then(function(disputeIDsHash) {
                    if (disputeIDsHash.length > 0) {
                        stateArray = [];
                        reasonArray = [];
                        if (disputeFilter.states) {
                            for (var i = 0; i < disputeFilter.states.length; i++) {
                                switch (disputeFilter.states[i]) {
                                    case "OPEN":
                                        stateArray.push(0);
                                        break;

                                    case "CLOSED":
                                        stateArray.push(1);
                                        break;
                                }
                            }
                        }
                        if (disputeFilter.reasons) {
                            for (var i = 0; i < disputeFilter.reasons.length; i++) {
                                switch (disputeFilter.reasons[i]) {
                                    case "INVALID":
                                        reasonArray.push(0);
                                        break;
                                }
                            }
                        }
                        disputeContract.methods.filterDisputeByDetail(disputeIDsHash, disputeFilter.submittedDateStart, disputeFilter.submittedDateEnd, disputeFilter.closedDateStart, cdisputeFilter.losedDateEnd, stateArray, reasonArray).call().then(function(disputeIds) {
                            if (disputeIds.length > 0) {
                                for (var i = 0; i < disputeIds.length; i++) {
                                    var dispute = {};
                                    disputeContract.methods.getDisputeHeader(disputeIds[i]).call().then(function(disputeHeaders) {
                                        dispute.disputeID = disputeIds[i];
                                        dispute.disputeParty = disputeHeaders[0];
                                        dispute.disputedTransactionID = disputeHeaders[1];
                                        dispute.disputedBusinessTransactionIDs = disputeHeaders[2];
                                        disputeContract.methods.getDisputedSummaryDetails(disputeIds[i]).call.then(function(disputeDetails) {
                                            dispute.submittedDate = disputeDetails[0];
                                            dispute.closedDate = disputeDetails[1];
                                            dispute.state = disputeDetails[2];
                                            dispute.reason = disputeDetails[3];
                                            arrayOfDisputes.push(dispute)
                                        });
                                    });
                                }
                            }
                        });
                    }
                });
                return arrayOfDisputes;
            },
            closeDispute: function(id) {
                disputeContract.methods.closeDispute(id).send();
            },
            getDisputeSubmissionWindowInMinutes: function() {
                return disputeContract.methods.getDisputeSubmissionWindowInMinutes().call();
            },
            setDisputeSubmissionWindowInMinutes: function(valueInMiniute) {
                disputeContract.methods.setDisputeSubmissionWindowInMinutes(valueInMiniute).send();
            },
            getDispute: function(id) {
                var dispute = {};
                disputeContract.methods.getDisputeHeader(id).call().then(function(disputeHeaders) {
                    dispute.disputeID = id;
                    dispute.disputePartyAddress = disputeHeaders[0];
                    dispute.disputedTransactionID = disputeHeaders[1];
                    dispute.disputedBusinessTransactionIDs = disputeHeaders[2];
                    disputeContract.methods.getDisputeDetail(id).call.then(function(disputeDetails) {
                        dispute.submittedDate = disputeDetails[0];
                        dispute.closedDate = disputeDetails[1];
                        dispute.state = disputeDetails[2];
                        dispute.reason = disputeDetails[3];
                    });
                });
                return dispute;
            },
            getOrchestrator: function() {
                return disputeContract.methods.orchestrator().call();
            }
        };
    }
};