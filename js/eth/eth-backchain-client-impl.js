'use strict';

var fs = require('fs');
var Web3 = require('web3');

module.exports = {
    /**
     * Constructs a new client
     * @param {url} HTTP URL of the blockchain (e.g. http://localhost:8545)
     */
    createContentBcClient: function(config) {
        var web3;
        if (config.web3Provider !== undefined) {
            web3 = new Web3(config.web3Provider);
        } else {
            web3 = new Web3(new Web3.providers.HttpProvider(config.url));
        }

        // Warning - this abi must be updated any time ContractBackchain.sol changes
        var abi = [{
                "constant": true,
                "inputs": [],
                "name": "hashCount",
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
                    "name": "index",
                    "type": "uint256"
                }],
                "name": "getHash",
                "outputs": [{
                    "name": "",
                    "type": "bytes32"
                }],
                "payable": false,
                "type": "function"
            },
            {
                "constant": true,
                "inputs": [{
                    "name": "hash",
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
                "constant": false,
                "inputs": [{
                    "name": "hash",
                    "type": "bytes32"
                }],
                "name": "post",
                "outputs": [],
                "payable": false,
                "type": "function"
            },
            {
                "constant": true,
                "inputs": [],
                "name": "orchestrator",
                "outputs": [{
                    "name": "",
                    "type": "address"
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
                "inputs": [],
                "payable": false,
                "type": "constructor"
            }
        ];

        if (!config.fromAddress) {
            if (config.privateKey) {
                config.fromAddress = web3.eth.accounts.privateKeyToAccount(config.privateKey).address;
            } else {
                //read-only access
                config.fromAddress = '0x0000000000000000000000000000000000000000';
            }
        }
        var contentContract = new web3.eth.Contract(abi, config.contentBackchainContractAddress, {
            from: config.fromAddress,
            gasPrice: config.gasPrice,
            gas: config.gas
        });
        return {
            config: config,
            hashCount: function() {
                return contentContract.methods.hashCount().call().then(function(result) {
                    return Promise.resolve(parseInt(result))
                });
            },
            post: function(hash) {
                return contentContract.methods.post(hash).send();
            },
            verify: function(hash) {
                return contentContract.methods.verify(hash).call();
            },
            getHash: function(index) {
                return contentContract.methods.getHash(index).call();
            },
            getOrchestrator: function() {
                return contentContract.methods.orchestrator().call();
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
                    "name": "id",
                    "type": "bytes32"
                }],
                "name": "getDisputeDetail",
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
                "constant": false,
                "inputs": [{
                    "name": "id",
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
                        "name": "disputingPartyAddress",
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
                "inputs": [],
                "name": "getOrchestrator",
                "outputs": [{
                    "name": "",
                    "type": "address"
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
                        "name": "ids",
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
                        "name": "stateValues",
                        "type": "uint256[]"
                    },
                    {
                        "name": "reasonValues",
                        "type": "uint256[]"
                    }
                ],
                "name": "filterDisputeByDetail",
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
                        "name": "ids",
                        "type": "bytes32[]"
                    },
                    {
                        "name": "disputingParties",
                        "type": "address[]"
                    },
                    {
                        "name": "disputedTransactionIDs",
                        "type": "bytes32[]"
                    },
                    {
                        "name": "disputedBusinessTransactionIDs",
                        "type": "bytes32[]"
                    }
                ],
                "name": "filterDisputeByHeaders",
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
            },
            {
                "anonymous": false,
                "inputs": [{
                        "indexed": true,
                        "name": "_from",
                        "type": "address"
                    },
                    {
                        "indexed": true,
                        "name": "_id",
                        "type": "bytes32"
                    },
                    {
                        "indexed": false,
                        "name": "_value",
                        "type": "uint256"
                    }
                ],
                "name": "LogTrace",
                "type": "event"
            }
        ];

        if (!config.fromAddress) {
            if (config.privateKey) {
                config.fromAddress = web3.eth.accounts.privateKeyToAccount(config.privateKey).address;
            } else {
                //read-only access
                config.fromAddress = '0x0000000000000000000000000000000000000000';
            }
        }
        var disputeContract = new web3.eth.Contract(abi, config.disputeBackchainContractAddress, {
            from: config.fromAddress,
            gasPrice: config.gasPrice,
            gas: config.gas
        });

        // private functions
        var isArray = function(obj) {
            return Object.prototype.toString.call(obj) === '[object Array]';
        };

        var isEmpty = function(obj) {
            return obj === null || obj === undefined || ((isArray(obj) && obj.length <= 0)) || (typeof obj === 'string' && obj.trim().length <= 0);
        };

        var isValidReasonCode = function(reason) {
            switch (reason) {
                case "HASH_NOT_FOUND":
                case "INPUT_DISPUTED":
                case "TRANSACTION_DATE_DISPUTED":
                case "TRANSACTION_PARTIES_DISPUTED":
                case "DISPUTE_BUSINESS_TRANSACTIONS":
                case "FINANCIAL_DISPUTED":
                    return true;
            }
            return false;
        };

        var isValidBytes = function(paramBytes) {
            if(isEmpty(paramBytes)) {
                return true;
            }
            if(isArray(paramBytes)){
                for(var i = 0; i < paramBytes.length; i++) {
                    if(!isValidBytes(paramBytes[i])) {
                        return false;
                    }
                }
            }
            else if(Object.prototype.toString.call(paramBytes) === "[object String]"){
                var charCode;
                for(var j = 0; j < paramBytes.length; j++) {
                    charCode = paramBytes.charCodeAt(j);
                    if(j == 1 && paramBytes.charCodeAt(0) === 48 && (charCode === 120 || charCode === 88)) {
                        continue;
                    }
                    if(!((charCode >= 48 && charCode <= 57) || (charCode >= 65 && charCode <= 70) || (charCode >= 97 && charCode <= 102))) {
                        return false;
                    }
                }
            }
            else if(Object.prototype.toString.call(paramBytes) === "[object Object]"){
                var propNames = Object.getOwnPropertyNames(paramBytes);
                for(var k = 0; k < propNames.length; k++) {
                    if(!isValidBytes(paramBytes[propNames[j]])) {
                        return false;
                    }
                }
            }
            return true;
        };

        var convertStringToByte = function(paramString) {
            return paramString.indexOf('0x') !== 0 ? '0x' + paramString : paramString;
        };

        var convertByteToString = function(paramBytes) {
            return paramBytes.indexOf('0x') === 0 ? paramBytes.substring(2) : paramBytes;
        };
        var convertByteArrayToStringArray = function(byteArray) {
            var returnStringArray = [];
            if (!isEmpty(byteArray)) {
                if (isArray(byteArray)) {
                    for (var i = 0; i < byteArray.length; i++) {
                        returnStringArray.push(convertByteToString(byteArray[i]))
                    }
                } else {
                    returnStringArray.push(convertByteToString(byteArray))
                }
            }
            return returnStringArray;
        };

        var convertStringArrayToByteArray = function(stringArray) {
            var returnByteArray = [];
            if (!isEmpty(stringArray)) {
                if (isArray(stringArray)) {
                    for (var i = 0; i < stringArray.length; i++) {
                        returnByteArray.push(convertStringToByte(stringArray[i]))
                    }
                } else {
                    returnByteArray.push(convertStringToByte(stringArray))
                }
            }
            return returnByteArray;
        };

        var convertProps = function(obj, isConvertToBytes) {
            var arrayObj = [];
            if (isEmpty(obj)) {
                return arrayObj;
            } else if (!isArray(obj)) {
                arrayObj.push(isConvertToBytes ? convertStringToByte(obj) : obj);
            } else {
                arrayObj = obj;
            }
            return isConvertToBytes ? convertStringArrayToByteArray(arrayObj) : arrayObj;
        };

        return {
            config: config,
            submitDispute: function(dispute) {
                var localDispute = JSON.parse(JSON.stringify(dispute));
                if (isEmpty(localDispute.disputingParty)) {
                    localDispute.disputingParty = config.fromAddress;
                } else if (convertStringToByte(config.fromAddress) !== convertStringToByte(localDispute.disputingParty)) {
                    return Promise.reject(new Error("Cannot submit dispute. DisputingParty does not match with creator address"));
                }
                if (isEmpty(localDispute.disputeId)) {
                    localDispute.disputeId = '0x0000000000000000000000000000000000000000000000000000000000000000';
                }
                else if(!isValidBytes(localDispute.disputeId)) {
                    return Promise.reject(new Error("disputeId is invalid byte string: " + localDispute.disputeId));
                }
                if (isEmpty(localDispute.disputedTransactionId)) {
                    return Promise.reject(new Error("disputedTransactionId is required. Cannot be null or empty" + localDispute.disputedTransactionId));
                }
                else if(!isValidBytes(localDispute.disputedTransactionId)) {
                    return Promise.reject(new Error("disputedTransactionId is invalid byte string: " + localDispute.disputedTransactionId));
                }
                if (isEmpty(localDispute.reason)) {
                    return Promise.reject(new Error("Reason is required. Cannot be null or empty"));
                }
                if (!isValidReasonCode(localDispute.reason)) {
                    return Promise.reject(new Error("Invalid Reason code :" + localDispute.reason + " valid reason are [HASH_NOT_FOUND, INPUT_DISPUTED, TRANSACTION_DATE_DISPUTED, TRANSACTION_PARTIES_DISPUTED, DISPUTE_BUSINESS_TRANSACTIONS, FINANCIAL_DISPUTED]"));
                }
                if(!isValidBytes(localDispute.disputedBusinessTransactionIds)) {
                    return Promise.reject(new Error("disputedBusinessTransactionIds is invalid byte string: " + localDispute.disputedBusinessTransactionIds));
                }
                return disputeContract.methods.submitDispute(convertStringToByte(localDispute.disputeId), convertStringToByte(localDispute.disputingParty), convertStringToByte(localDispute.disputedTransactionId), convertStringArrayToByteArray(localDispute.disputedBusinessTransactionIds), localDispute.reason).send();
            },
            closeDispute: function(disputeId) {
                if (isEmpty(disputeId)) {
                    return Promise.reject(new Error("disputeId is required. Cannot be null or empty"));
                }
                if (!isValidBytes(disputeId)) {
                    return Promise.reject(new Error("disputeId is invalid byte string: " + disputeId));
                }
                return disputeContract.methods.closeDispute(convertStringToByte(disputeId)).send();
            },
            getDisputeSubmissionWindowInMinutes: function() {
                return disputeContract.methods.getDisputeSubmissionWindowInMinutes().call();
            },
            setDisputeSubmissionWindowInMinutes: function(valueInMiniute) {
                return disputeContract.methods.setDisputeSubmissionWindowInMinutes(valueInMiniute).send();
            },
            getDispute: function(disputeId) {
                if (isEmpty(disputeId)) {
                    return Promise.reject(new Error("disputeId is required. Cannot be null or empty"));
                }
                if (!isValidBytes(disputeId)) {
                    return Promise.reject(new Error("disputeId is invalid byte string: " + disputeId));
                }
                var dispute = {};
                dispute.disputeId = convertStringToByte(disputeId);
                return disputeContract.methods.getDisputeHeader(dispute.disputeId).call().then(function(disputeHeaders) {
                    dispute.disputingParty = disputeHeaders[0];
                    dispute.disputedTransactionId = disputeHeaders[1];
                    dispute.disputedBusinessTransactionIds = disputeHeaders[2];
                    return disputeContract.methods.getDisputeDetail(dispute.disputeId).call();
                }).then(function(disputeDetails) {
                    dispute.submittedDate = parseInt(disputeDetails[0]);
                    dispute.closedDate = disputeDetails[1] === '0' || disputeDetails[1] === 0 ? null : parseInt(disputeDetails[1]);
                    dispute.state = disputeDetails[2];
                    dispute.reason = disputeDetails[3];
                    return Promise.resolve(dispute);
                });
            },
            getOrchestrator: function() {
                return disputeContract.methods.getOrchestrator().call();
            },
            getFilterDisputeIds: function(disputeFilter) {
                if (!isValidBytes(disputeFilter.disputeId)) {
                    return Promise.reject(new Error("disputeId is invalid byte string: " + disputeFilter.disputeId));
                }
                if (!isValidBytes(disputeFilter.disputingParty)) {
                    return Promise.reject(new Error("disputingParty is invalid byte string: " + disputeFilter.disputingParty));
                }
                if (!isValidBytes(disputeFilter.disputedTransactionId)) {
                    return Promise.reject(new Error("disputedTransactionId is invalid byte string: " + disputeFilter.disputedTransactionId));
                }
                if (!isValidBytes(disputeFilter.disputedBusinessTransactionIds)) {
                    return Promise.reject(new Error("disputedBusinessTransactionIds is invalid byte string: " + disputeFilter.disputedBusinessTransactionIds));
                }
                var filter = JSON.parse(JSON.stringify(disputeFilter));
                filter.disputeId = convertProps(filter.disputeId, true);
                filter.disputingParty = convertProps(filter.disputingParty, true);
                filter.disputedTransactionId = convertProps(filter.disputedTransactionId, true);
                filter.disputedBusinessTransactionIds = convertProps(filter.disputedBusinessTransactionIds, true);
                return disputeContract.methods.filterDisputeByHeaders(filter.disputeId, filter.disputingParty, filter.disputedTransactionId, filter.disputedBusinessTransactionIds).call().then(function(disputeIDsHash) {
                    if (disputeIDsHash.length <= 0) {
                        return Promise.resolve([]);
                    }
                    if (isEmpty(filter.submittedDateStart)) {
                        filter.submittedDateStart = 0;
                    }
                    if (isEmpty(filter.submittedDateEnd)) {
                        filter.submittedDateEnd = 0;
                    }
                    if (isEmpty(filter.closedDateStart)) {
                        filter.closedDateStart = 0;
                    }
                    if (isEmpty(filter.closedDateEnd)) {
                        filter.closedDateEnd = 0;
                    }
                    var stateArray = [];
                    filter.state = convertProps(filter.state, false);
                    for (var i = 0; i < filter.state.length; i++) {
                        switch (filter.state[i]) {
                            case "OPEN":
                                stateArray.push(0);
                                break;

                            case "CLOSED":
                                stateArray.push(1);
                                break;

                            default:
                                return Promise.reject(new Error("Invalid State code :" + filter.states[i] + " valid states are [OPEN, CLOSED]"));
                        }
                    }
                    var reasonArray = [];
                    filter.reason = convertProps(filter.reason, false);
                    for (var i = 0; i < filter.reason.length; i++) {
                        switch (filter.reason[i]) {
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

                            default:
                                return Promise.reject(new Error("Invalid Reason code :" + filter.reasons[i] + " valid reason are [HASH_NOT_FOUND, INPUT_DISPUTED, TRANSACTION_DATE_DISPUTED, TRANSACTION_PARTIES_DISPUTED, DISPUTE_BUSINESS_TRANSACTIONS, FINANCIAL_DISPUTED]"));
                        }
                    }
                    return disputeContract.methods.filterDisputeByDetail(disputeIDsHash, filter.submittedDateStart, filter.submittedDateEnd, filter.closedDateStart, filter.closedDateEnd, stateArray, reasonArray).call();
                });
            },
            getDisputeCount: function(disputeFilter) {
                return this.getFilterDisputeIds(disputeFilter).then(function(disputeIds) {
                    return Promise.resolve(disputeIds.length);
                });
            },
            filterDisputes: function(disputeFilter) {
                var me = this;
                return this.getFilterDisputeIds(disputeFilter).then(function(disputeIds) {
                    if (disputeIds.length <= 0) {
                        return Promise.resolve([]);
                    }
                    var arrayOfDisputePromises = [];
                    var disputePromiseFn = function(disputeId) {
                        return new Promise(function(resolve) {
                            resolve(me.getDispute(disputeId));
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
}
