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
        if (config.web3Provider !== undefined) {
            web3 = new Web3(config.web3Provider);
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
    createDisputeBcClient: function(config) {
        if (config.blockchain != 'eth') {
            throw new Error('blockchain not supported: ' + config.blockchain);
        }
        var web3;
        if (config.web3Provider !== undefined) {
            web3 = new Web3(config.web3Provider);
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
            from: config.fromAddress,
            gas: 1000000
        });

        return {
            config: config,
            submitDispute: function(dispute) {
                if (dispute.disputingParty === undefined) {
                    dispute.disputingParty = config.fromAddress;
                }
                if (dispute.id === undefined) {
                    dispute.id = '0x0000000000000000000000000000000000000000000000000000000000000000';
                }
                if (dispute.disputedBusinessTransactionIDs === undefined) {
                    dispute.disputedBusinessTransactionIDs = [];
                }
                return disputeContract.methods.submitDispute(dispute.id, dispute.disputingParty, dispute.disputedTransactionID, dispute.disputedBusinessTransactionIDs, dispute.reason).send();
            },
            closeDispute: function(disputeIDHash) {
                return disputeContract.methods.closeDispute(disputeIDHash).send();
            },
            getDisputeSubmissionWindowInMinutes: function() {
                return disputeContract.methods.getDisputeSubmissionWindowInMinutes().call();
            },
            setDisputeSubmissionWindowInMinutes: function(valueInMiniute) {
                return disputeContract.methods.setDisputeSubmissionWindowInMinutes(valueInMiniute).send();
            },
            getDispute: function(disputeIDHash) {
                var dispute = {};
                return disputeContract.methods.getDisputeHeader(disputeIDHash).call().then(function(disputeHeaders) {
                    dispute.disputeID = disputeIDHash;
                    dispute.disputingParty = disputeHeaders[0];
                    dispute.disputedTransactionID = disputeHeaders[1];
                    dispute.disputedBusinessTransactionIDs = disputeHeaders[2];
                    return disputeContract.methods.getDisputeDetail(disputeIDHash).call();
                }).then(function(disputeDetails) {
                    dispute.submittedDate = disputeDetails[0];
                    dispute.closedDate = disputeDetails[1];
                    dispute.state = disputeDetails[2];
                    dispute.reason = disputeDetails[3];
                    return Promise.resolve(dispute);
                });
            },
            getOrchestrator: function() {
                return disputeContract.methods.getOrchestrator().call();
            },
            filterDisputes: function(disputeFilter) {
                if (disputeFilter.disputeIDs === undefined) {
                    disputeFilter.disputeIDs = [];
                }
                if (disputeFilter.disputingParties === undefined) {
                    disputeFilter.disputingParties = [];
                }
                if (disputeFilter.disputedTransactionIDs === undefined) {
                    disputeFilter.disputedTransactionIDs = [];
                }
                if (disputeFilter.disputedBusinessTransactionIDs === undefined) {
                    disputeFilter.disputedBusinessTransactionIDs = [];
                }
                return disputeContract.methods.filterDisputeByHeaders(disputeFilter.disputeIDs, disputeFilter.disputingParties, disputeFilter.disputedTransactionIDs, disputeFilter.disputedBusinessTransactionIDs).call().then(function(disputeIDsHash) {
                    if (disputeIDsHash.length <= 0) {
                        return [];
                    }
                    if (disputeFilter.submittedDateStart === undefined) {
                        disputeFilter.submittedDateStart = 0;
                    }
                    if (disputeFilter.submittedDateEnd === undefined) {
                        disputeFilter.submittedDateEnd = 0;
                    }
                    if (disputeFilter.closedDateStart === undefined) {
                        disputeFilter.closedDateStart = 0;
                    }
                    if (disputeFilter.closedDateEnd === undefined) {
                        disputeFilter.closedDateEnd = 0;
                    }
                    var stateArray = [];
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
                    var reasonArray = [];
                    if (disputeFilter.reasons) {
                        for (var i = 0; i < disputeFilter.reasons.length; i++) {
                            switch (disputeFilter.reasons[i]) {
                                case "HASH_NOT_FOUND":
                                    reasonArray.push(0);
                                    break;

                                case "INPUT_DISPUTED":
                                    reasonArray.push(1);
                                    break;

                                case "TRANSACTION_DATE_DISPUTED":
                                    reasonArray.push(2);
                                    break;

                                case "TRANSACTION_PARTIES_DISPUTED":
                                    reasonArray.push(3);
                                    break;

                                case "DISPUTE_BUSINESS_TRANSACTIONS":
                                    reasonArray.push(4);
                                    break;

                                case "FINANCIAL_DISPUTED":
                                    reasonArray.push(5);
                                    break;
                            }
                        }
                    }
                    return disputeContract.methods.filterDisputeByDetail(disputeIDsHash, disputeFilter.submittedDateStart, disputeFilter.submittedDateEnd, disputeFilter.closedDateStart, disputeFilter.closedDateEnd, stateArray, reasonArray).call();
                }).then(function(disputeIds) {
                    if (disputeIds.length <= 0) {
                        return Promise.resolve(disputeIds);
                    }
                    var arrayOfDisputePromises = [];
                    var disputePromiseFn = function(disputeId) {
                        return new Promise((resolve) => {
                            var dispute = {};
                            return disputeContract.methods.getDisputeHeader(disputeId).call().then(function(disputeHeaders) {
                                dispute.disputeID = disputeId;
                                dispute.disputingParty = disputeHeaders[0];
                                dispute.disputedTransactionID = disputeHeaders[1];
                                dispute.disputedBusinessTransactionIDs = disputeHeaders[2];
                                return disputeContract.methods.getDisputeDetail(disputeId).call();
                            }).then(function(disputeDetails) {
                                dispute.submittedDate = disputeDetails[0];
                                dispute.closedDate = disputeDetails[1];
                                dispute.state = disputeDetails[2];
                                dispute.reason = disputeDetails[3];
                                resolve(dispute);
                            });
                        });

                    };
                    for (var i = 0; i < disputeIds.length; i++) {
                        arrayOfDisputePromises.push(disputePromiseFn(disputeIds[i]));
                    }
                    return Promise.all(arrayOfDisputePromises);
                });
            }
        };
    }
};