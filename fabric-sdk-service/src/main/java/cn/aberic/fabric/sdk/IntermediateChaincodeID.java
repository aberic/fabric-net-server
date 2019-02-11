/*
 * Copyright (c) 2018. Aberic - aberic@qq.com - All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.aberic.fabric.sdk;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 中继合约对象
 *
 * @author 杨毅 【2017年10月18日 - 14:07】
 */
class IntermediateChaincodeID {

    private Logger log = LogManager.getLogger(IntermediateChaincodeID.class);

    /** 智能合约名称 */
    private String chaincodeName; // mycc
    /** 可能是包含智能合约的go环境路径 */
    private String chaincodeSource; // /opt/gopath
    /** 智能合约安装路径 */
    private String chaincodePath; // github.com/hyperledger/fabric/xxx/chaincode/go/example/test
    /** 智能合约背书策略文件存放路径 */
    private String chaincodePolicy; // /home/policy.yaml
    /** 智能合约版本号 */
    private String chaincodeVersion; // 1.0
    /** 指定ID的智能合约 */
    private ChaincodeID chaincodeID;
    /** 单个提案请求的超时时间以毫秒为单位 */
    private int proposalWaitTime = 200000;

    void setChaincodeName(String chaincodeName) {
        this.chaincodeName = chaincodeName;
        setChaincodeID();
    }

    void setChaincodeSource(String chaincodeSource) {
        this.chaincodeSource = chaincodeSource;
        setChaincodeID();
    }

    void setChaincodePath(String chaincodePath) {
        this.chaincodePath = chaincodePath;
        setChaincodeID();
    }

    void setChaincodePolicy(String chaincodePolicy) {
        this.chaincodePolicy = chaincodePolicy;
    }

    void setChaincodeVersion(String chaincodeVersion) {
        this.chaincodeVersion = chaincodeVersion;
        setChaincodeID();
    }

    private void setChaincodeID() {
        if (null != chaincodeName && null != chaincodePath && null != chaincodeVersion) {
            chaincodeID = ChaincodeID.newBuilder().setName(chaincodeName).setVersion(chaincodeVersion).setPath(chaincodePath).build();
        }
    }

    /**
     * 安装智能合约
     *
     * @param org 中继组织对象
     */
    JSONObject install(IntermediateOrg org) throws ProposalException, InvalidArgumentException, TransactionException {
        /// Send transaction proposal to all peers
        InstallProposalRequest installProposalRequest = org.getClient().newInstallProposalRequest();
        installProposalRequest.setChaincodeName(chaincodeName);
        installProposalRequest.setChaincodeVersion(chaincodeVersion);
        installProposalRequest.setChaincodeSourceLocation(new File(chaincodeSource));
        installProposalRequest.setChaincodePath(chaincodePath);
        installProposalRequest.setChaincodeLanguage(TransactionRequest.Type.GO_LANG);
        installProposalRequest.setProposalWaitTime(proposalWaitTime);

        long currentStart = System.currentTimeMillis();
        Collection<ProposalResponse> installProposalResponses = org.getClient().sendInstallProposal(installProposalRequest, org.getChannel().get().getPeers());
        log.info("chaincode install transaction proposal time = " + (System.currentTimeMillis() - currentStart));
        return toPeerResponse(installProposalResponses, false);
    }

    /**
     * 实例化智能合约
     *
     * @param org  中继组织对象
     * @param args 初始化参数数组
     */
    JSONObject instantiate(IntermediateOrg org, String[] args) throws ProposalException, InvalidArgumentException, IOException, ChaincodeEndorsementPolicyParseException, TransactionException {
        /// Send transaction proposal to all peers
        InstantiateProposalRequest instantiateProposalRequest = org.getClient().newInstantiationProposalRequest();
        instantiateProposalRequest.setChaincodeID(chaincodeID);
        instantiateProposalRequest.setProposalWaitTime(proposalWaitTime);
        instantiateProposalRequest.setArgs(args);

        ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
        chaincodeEndorsementPolicy.fromYamlFile(new File(chaincodePolicy));
        instantiateProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);

        Map<String, byte[]> tm2 = new HashMap<>();
        tm2.put("HyperLedgerFabric", "InstantiateProposalRequest:JavaSDK".getBytes(UTF_8));
        tm2.put("method", "InstantiateProposalRequest".getBytes(UTF_8));
        tm2.put("result", ":)".getBytes(UTF_8));
        instantiateProposalRequest.setTransientMap(tm2);

        long currentStart = System.currentTimeMillis();
        Collection<ProposalResponse> instantiateProposalResponses = org.getChannel().get().sendInstantiationProposal(instantiateProposalRequest, org.getChannel().get().getPeers());
        log.info("chaincode instantiate transaction proposal time = " + (System.currentTimeMillis() - currentStart));
        return toOrdererResponse(instantiateProposalResponses, org);
    }

    /**
     * 升级智能合约
     *
     * @param org  中继组织对象
     * @param args 初始化参数数组
     */
    JSONObject upgrade(IntermediateOrg org, String[] args) throws ProposalException, InvalidArgumentException, IOException, ChaincodeEndorsementPolicyParseException, TransactionException {
        /// Send transaction proposal to all peers
        UpgradeProposalRequest upgradeProposalRequest = org.getClient().newUpgradeProposalRequest();
        upgradeProposalRequest.setChaincodeID(chaincodeID);
        upgradeProposalRequest.setProposalWaitTime(proposalWaitTime);
        upgradeProposalRequest.setArgs(args);

        ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
        chaincodeEndorsementPolicy.fromYamlFile(new File(chaincodePolicy));
        upgradeProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);

        Map<String, byte[]> tm2 = new HashMap<>();
        tm2.put("HyperLedgerFabric", "UpgradeProposalRequest:JavaSDK".getBytes(UTF_8));
        tm2.put("method", "UpgradeProposalRequest".getBytes(UTF_8));
        tm2.put("result", ":)".getBytes(UTF_8));
        upgradeProposalRequest.setTransientMap(tm2);

        long currentStart = System.currentTimeMillis();
        Collection<ProposalResponse> upgradeProposalResponses = org.getChannel().get().sendUpgradeProposal(upgradeProposalRequest, org.getChannel().get().getPeers());
        log.info("chaincode instantiate transaction proposal time = " + (System.currentTimeMillis() - currentStart));
        return toOrdererResponse(upgradeProposalResponses, org);
    }

    /**
     * 执行智能合约
     *
     * @param org  中继组织对象
     * @param fcn  方法名
     * @param args 参数数组
     */
    JSONObject invoke(IntermediateOrg org, String fcn, String[] args) throws InvalidArgumentException, ProposalException, IOException, TransactionException {
        /// Send transaction proposal to all peers
        TransactionProposalRequest transactionProposalRequest = org.getClient().newTransactionProposalRequest();
        transactionProposalRequest.setChaincodeID(chaincodeID);
        transactionProposalRequest.setFcn(fcn);
        transactionProposalRequest.setArgs(args);
        transactionProposalRequest.setProposalWaitTime(proposalWaitTime);

        Map<String, byte[]> tm2 = new HashMap<>();
        tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
        tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
        tm2.put("result", ":)".getBytes(UTF_8));
        transactionProposalRequest.setTransientMap(tm2);

        long currentStart = System.currentTimeMillis();
        Collection<ProposalResponse> transactionProposalResponses = org.getChannel().get().sendTransactionProposal(transactionProposalRequest, org.getChannel().get().getPeers());
        log.info("chaincode invoke transaction proposal time = " + (System.currentTimeMillis() - currentStart));
        return toOrdererResponse(transactionProposalResponses, org);
    }

    /**
     * 查询智能合约
     *
     * @param org     中继组织对象
     * @param fcn     方法名
     * @param args    参数数组
     */
    JSONObject query(IntermediateOrg org, String fcn, String[] args) throws InvalidArgumentException, ProposalException, TransactionException {
        QueryByChaincodeRequest queryByChaincodeRequest = org.getClient().newQueryProposalRequest();
        queryByChaincodeRequest.setArgs(args);
        queryByChaincodeRequest.setFcn(fcn);
        queryByChaincodeRequest.setChaincodeID(chaincodeID);
        queryByChaincodeRequest.setProposalWaitTime(proposalWaitTime);

        Map<String, byte[]> tm2 = new HashMap<>();
        tm2.put("HyperLedgerFabric", "QueryByChaincodeRequest:JavaSDK".getBytes(UTF_8));
        tm2.put("method", "QueryByChaincodeRequest".getBytes(UTF_8));
        queryByChaincodeRequest.setTransientMap(tm2);

        long currentStart = System.currentTimeMillis();
        Collection<ProposalResponse> queryProposalResponses = org.getChannel().get().queryByChaincode(queryByChaincodeRequest, org.getChannel().get().getPeers());
        log.info("chaincode query transaction proposal time = " + (System.currentTimeMillis() - currentStart));
        return toPeerResponse(queryProposalResponses, true);
    }

    /**
     * 获取实例化合约、升级合约以及invoke合约的返回结果集合
     *
     * @param proposalResponses 请求返回集合
     * @param org               中继组织对象
     */
    private JSONObject toOrdererResponse(Collection<ProposalResponse> proposalResponses, IntermediateOrg org) throws InvalidArgumentException, UnsupportedEncodingException, TransactionException {
        JSONObject jsonObject = new JSONObject();
        ProposalResponse first = null;
        Collection<ProposalResponse> successful = new LinkedList<>();
        Collection<ProposalResponse> failed = new LinkedList<>();
        for (ProposalResponse response : proposalResponses) {
            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                successful.add(response);
            } else {
                failed.add(response);
            }
        }

        Collection<Set<ProposalResponse>> proposalConsistencySets = SDKUtils.getProposalConsistencySets(proposalResponses);
        if (proposalConsistencySets.size() != 1) {
            log.error("Expected only one set of consistent proposal responses but got " + proposalConsistencySets.size());
        }
        if (failed.size() > 0) {
            for (ProposalResponse fail : failed) {
                log.error("Not enough endorsers for instantiate :" + successful.size() + "endorser failed with " + fail.getMessage() + ", on peer" + fail.getPeer());
            }
            first = failed.iterator().next();
            log.error("Not enough endorsers for inspect:" + failed.size() + " endorser error: " + first.getMessage() + ". Was verified: "
                    + first.isVerified());
        }
        if (successful.size() > 0) {
            log.info("Successfully received transaction proposal responses.");
            ProposalResponse resp = proposalResponses.iterator().next();
            log.debug("TransactionID: " + resp.getTransactionID());
            byte[] x = resp.getChaincodeActionResponsePayload();
            String resultAsString = null;
            if (x != null) {
                resultAsString = new String(x, "UTF-8");
            }
            log.info("resultAsString = " + resultAsString);
            // org.getChannel().get().sendTransaction(successful).get(transactionWaitTime, TimeUnit.SECONDS);
            org.getChannel().get().sendTransaction(successful);
            jsonObject = parseResult(resultAsString);
            jsonObject.put("code", BlockListener.SUCCESS);
            jsonObject.put("txid", resp.getTransactionID());
            return jsonObject;
        } else {
            jsonObject.put("code", BlockListener.ERROR);
            jsonObject.put("error", null != first ? first.getMessage() : "error unknown");
            return jsonObject;
        }
//        channel.sendTransaction(successful).thenApply(transactionEvent -> {
//            if (transactionEvent.isValid()) {
//                log.info("Successfully send transaction proposal to orderer. Transaction ID: " + transactionEvent.getTransactionID());
//            } else {
//                log.info("Failed to send transaction proposal to orderer");
//            }
//            // chain.shutdown(true);
//            return transactionEvent.getTransactionID();
//        }).get(chaincode.getInvokeWatiTime(), TimeUnit.SECONDS);
    }

    /**
     * 获取安装合约以及query合约的返回结果集合
     *
     * @param proposalResponses 请求返回集合
     * @param checkVerified     是否验证提案
     */
    private JSONObject toPeerResponse(Collection<ProposalResponse> proposalResponses, boolean checkVerified) {
        JSONObject jsonObject = new JSONObject();
        for (ProposalResponse proposalResponse : proposalResponses) {
            if ((checkVerified && !proposalResponse.isVerified()) || proposalResponse.getStatus() != ProposalResponse.Status.SUCCESS) {
                String data = String.format("Failed install/query proposal from peer %s status: %s. Messages: %s. Was verified : %s",
                        proposalResponse.getPeer().getName(), proposalResponse.getStatus(), proposalResponse.getMessage(), proposalResponse.isVerified());
                log.debug(data);
                jsonObject.put("code", BlockListener.ERROR);
                jsonObject.put("error", data);
            } else {
                String payload = proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
                log.debug("Install/Query payload from peer: " + proposalResponse.getPeer().getName());
                log.debug("TransactionID: " + proposalResponse.getTransactionID());
                log.debug("" + payload);
                jsonObject = parseResult(payload);
                jsonObject.put("code", BlockListener.SUCCESS);
                jsonObject.put("txid", proposalResponse.getTransactionID());
            }
        }
        return jsonObject;
    }

    /**
     * 设置单个提案请求的超时时间以毫秒为单位
     *
     * @param proposalWaitTime 超时时间以毫秒为单位
     */
    void setProposalWaitTime(int proposalWaitTime) {
        this.proposalWaitTime = proposalWaitTime;
    }

    private JSONObject parseResult(String result) {
        JSONObject jsonObject = new JSONObject();
        int jsonVerify = isJSONValid(result);
        switch (jsonVerify) {
            case 0:
                jsonObject.put("data", result);
                break;
            case 1:
                jsonObject.put("data", JSONObject.parseObject(result));
                break;
            case 2:
                jsonObject.put("data", JSONObject.parseArray(result));
                break;
        }
        return jsonObject;
    }

    /**
     * 判断字符串类型
     *
     * @param str 字符串
     *
     * @return 0-string；1-JsonObject；2、JsonArray
     */
    private static int isJSONValid(String str) {
        try {
            JSONObject.parseObject(str);
            return 1;
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(str);
                return 2;
            } catch (JSONException ex1) {
                return 0;
            }
        }
    }

}
