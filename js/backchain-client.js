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

        config.fromAddress = web3.eth.accounts.privateKeyToAccount(config.privateKey).address;
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
    };

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
                "inputs": [],
                "name": "getDisputeCount",
                "outputs": [{
                    "name": "",
                    "type": "uint256"
                }],
                "payable": false,
                "type": "function"
            },
            {
                "constant": true,
                "inputs": [{
                    "name": "hashID",
                    "type": "bytes32"
                }],
                "name": "getDisputeBasicDetail",
                "outputs": [{
                        "name": "",
                        "type": "address"
                    },
                    {
                        "name": "",
                        "type": "bytes32"
                    },
                    {
                        "name": "",
                        "type": "bytes32[]"
                    }
                ],
                "payable": false,
                "type": "function"
            },
            {
                "constant": true,
                "inputs": [{
                    "name": "hashID",
                    "type": "bytes32"
                }],
                "name": "getDisputedSummaryDetails",
                "outputs": [{
                        "name": "",
                        "type": "uint256"
                    },
                    {
                        "name": "",
                        "type": "uint256"
                    },
                    {
                        "name": "",
                        "type": "string"
                    },
                    {
                        "name": "",
                        "type": "string"
                    }
                ],
                "payable": false,
                "type": "function"
            },
            {
                "constant": true,
                "inputs": [{
                        "name": "hashIDs",
                        "type": "bytes32[]"
                    },
                    {
                        "name": "stateValue",
                        "type": "string"
                    },
                    {
                        "name": "reasonValue",
                        "type": "string"
                    }
                ],
                "name": "findDispute",
                "outputs": [{
                    "name": "",
                    "type": "bytes32[]"
                }],
                "payable": false,
                "type": "function"
            },
            {
                "constant": false,
                "inputs": [{
                    "name": "hashID",
                    "type": "bytes32"
                }],
                "name": "closeDispute",
                "outputs": [],
                "payable": false,
                "type": "function"
            },
            {
                "constant": false,
                "inputs": [{
                        "name": "disputeID",
                        "type": "bytes32"
                    },
                    {
                        "name": "disputePartyAddress",
                        "type": "address"
                    },
                    {
                        "name": "disputedTransactionID",
                        "type": "bytes32"
                    },
                    {
                        "name": "disputedBusinessTransactionIDs",
                        "type": "bytes32[]"
                    },
                    {
                        "name": "reasonCode",
                        "type": "string"
                    }
                ],
                "name": "submitDispute",
                "outputs": [],
                "payable": false,
                "type": "function"
            },
            {
                "constant": true,
                "inputs": [{
                        "name": "hashIDs",
                        "type": "bytes32[]"
                    },
                    {
                        "name": "submittedDateStart",
                        "type": "uint256"
                    },
                    {
                        "name": "submittedDateEnd",
                        "type": "uint256"
                    },
                    {
                        "name": "closedDateStart",
                        "type": "uint256"
                    },
                    {
                        "name": "closedDateEnd",
                        "type": "uint256"
                    }
                ],
                "name": "findDispute",
                "outputs": [{
                    "name": "",
                    "type": "bytes32[]"
                }],
                "payable": false,
                "type": "function"
            },
            {
                "constant": true,
                "inputs": [{
                    "name": "hashID",
                    "type": "bytes32"
                }],
                "name": "verify",
                "outputs": [{
                    "name": "",
                    "type": "bool"
                }],
                "payable": false,
                "type": "function"
            },
            {
                "constant": true,
                "inputs": [{
                        "name": "hashIDs",
                        "type": "bytes32[]"
                    },
                    {
                        "name": "disputePartyAddress",
                        "type": "address"
                    },
                    {
                        "name": "disputedTransactionID",
                        "type": "bytes32"
                    },
                    {
                        "name": "disputedBusinessTransactionIDs",
                        "type": "bytes32[]"
                    }
                ],
                "name": "findDispute",
                "outputs": [{
                    "name": "",
                    "type": "bytes32[]"
                }],
                "payable": false,
                "type": "function"
            },
            {
                "constant": true,
                "inputs": [{
                        "name": "hashIDs",
                        "type": "bytes32[]"
                    },
                    {
                        "name": "disputingParty",
                        "type": "address"
                    }
                ],
                "name": "findDispute",
                "outputs": [{
                    "name": "",
                    "type": "bytes32[]"
                }],
                "payable": false,
                "type": "function"
            },
            {
                "constant": false,
                "inputs": [{
                    "name": "valInMinute",
                    "type": "uint256"
                }],
                "name": "setDisputeSubmissionWindowInMinutes",
                "outputs": [],
                "payable": false,
                "type": "function"
            },
            {
                "constant": true,
                "inputs": [{
                        "name": "hashIDs",
                        "type": "bytes32[]"
                    },
                    {
                        "name": "submittedDateStart",
                        "type": "uint256"
                    },
                    {
                        "name": "submittedDateEnd",
                        "type": "uint256"
                    },
                    {
                        "name": "closedDateStart",
                        "type": "uint256"
                    },
                    {
                        "name": "closedDateEnd",
                        "type": "uint256"
                    },
                    {
                        "name": "stateValue",
                        "type": "string"
                    },
                    {
                        "name": "reasonValue",
                        "type": "string"
                    }
                ],
                "name": "findDispute",
                "outputs": [{
                    "name": "",
                    "type": "bytes32[]"
                }],
                "payable": false,
                "type": "function"
            },
            {
                "constant": true,
                "inputs": [],
                "name": "getDisputIDs",
                "outputs": [{
                    "name": "",
                    "type": "bytes32[]"
                }],
                "payable": false,
                "type": "function"
            },
            {
                "constant": true,
                "inputs": [],
                "name": "getDisputeSubmissionWindowInMinutes",
                "outputs": [{
                    "name": "",
                    "type": "uint256"
                }],
                "payable": false,
                "type": "function"
            },
            {
                "constant": true,
                "inputs": [{
                        "name": "hashIDs",
                        "type": "bytes32[]"
                    },
                    {
                        "name": "disputedTransactionID",
                        "type": "bytes32"
                    }
                ],
                "name": "findDispute",
                "outputs": [{
                    "name": "",
                    "type": "bytes32[]"
                }],
                "payable": false,
                "type": "function"
            },
            {
                "constant": true,
                "inputs": [{
                    "name": "",
                    "type": "bytes32"
                }],
                "name": "hashMapping",
                "outputs": [{
                    "name": "",
                    "type": "bool"
                }],
                "payable": false,
                "type": "function"
            },
            {
                "constant": true,
                "inputs": [{
                        "name": "hashIDs",
                        "type": "bytes32[]"
                    },
                    {
                        "name": "disputedBusinessTransactionIds",
                        "type": "bytes32[]"
                    }
                ],
                "name": "findDispute",
                "outputs": [{
                    "name": "",
                    "type": "bytes32[]"
                }],
                "payable": false,
                "type": "function"
            },
            {
                "inputs": [],
                "payable": false,
                "type": "constructor"
            }
        ];

        config.fromAddress = web3.eth.accounts.privateKeyToAccount(config.privateKey).address;
        var contentContract = new web3.eth.Contract(abi, config.contentBackchainContractAddress, {
            from: config.fromAddress
        });
        var disputeContract = new web3.eth.Contract(abi, config.disputeBackchainContractAddress, {
            from: config.fromAddress
        });
        return {
            config: config,
            getDisputeCount: function() {
                return disputeContract.methods.getDisputeCount().call().then(function(result) {
                    return Promise.resolve(parseInt(result))
                });
            },
            submitDispute: function(disputeID, disputePartyAddress, disputedTransactionID, disputedBusinessTransactionIDs, reasonCode) {
                return disputeContract.methods.submitDispute(disputeID, disputePartyAddress, disputedTransactionID, disputedBusinessTransactionIDs, reasonCode).send();
            },
            findDispute: function(hash, disputeParty, disputedTransactionID, disputedBusinessTransactionIDs, submittedDateStart, submittedDateEnd, closedDateStart, closedDateEnd, stateValue, reasonValue) {
                var arrayOfDisputes = [];
                disputeContract.methods.findDispute(hash, disputeParty, disputedTransactionID, disputedBusinessTransactionIDs).call().then(function(disputeIDsHash) {
                    if (disputeIDsHash.length > 0) {
                        disputeContract.methods.findDispute(disputeIDsHash, submittedDateStart, submittedDateEnd, closedDateStart, closedDateEnd, stateValue, reasonValue).call().then(function(hashArray) {
                            if (hashArray.length > 0) {
                                for (var i = 0; i < hashArray.length; i++) {
                                    var dispute = {};
                                    disputeContract.methods.getDisputeBasicDetail(hashArray[i]).call().then(function(disputeID, disputePartyAddress, disputedTransactionID, disputedBusinessTransactionIDs) {
                                        dispute.disputeID = disputeID;
                                        dispute.disputePartyAddress = disputePartyAddress;
                                        dispute.disputedTransactionID = disputedTransactionID;
                                        dispute.disputedBusinessTransactionIDs = disputedBusinessTransactionIDs;
                                        getDisputedSummaryDetails(hashArray[i]).call.then(function(submittedDate, closedDate, state, reason) {
                                            dispute.submittedDate = submittedDate;
                                            dispute.closedDate = closedDate;
                                            dispute.state = state;
                                            dispute.reason = reason;
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
            verify: function(hash) {
                return disputeContract.methods.verify(hash).call();
            },
            closeDispute: function(hash) {
                disputeContract.methods.closeDispute(hash).call();
            },
            getDisputeSubmissionWindowInMinutes: function() {
                return disputeContract.methods.getDisputeSubmissionWindowInMinutes().call();
            },
            setDisputeSubmissionWindowInMinutes: function(valueInMiniute) {
                disputeContract.methods.setDisputeSubmissionWindowInMinutes(valueInMiniute).call();
            },
            getDispute: function(hash) {
                var dispute = {};
                disputeContract.methods.getDisputeBasicDetail(hash).call().then(function(disputeID, disputePartyAddress, disputedTransactionID, disputedBusinessTransactionIDs) {
                    dispute.disputeID = disputeID;
                    dispute.disputePartyAddress = disputePartyAddress;
                    dispute.disputedTransactionID = disputedTransactionID;
                    dispute.disputedBusinessTransactionIDs = disputedBusinessTransactionIDs;
                    getDisputedSummaryDetails(hash).call.then(function(submittedDate, closedDate, state, reason) {
                        dispute.submittedDate = submittedDate;
                        dispute.closedDate = closedDate;
                        dispute.state = state;
                        dispute.reason = reason;
                    });
                });
                return dispute;
            },
            getOrchestrator: function() {
                return disputeContract.methods.orchestrator().call();
            }
        };
    };
};
