package org.hyperledger.fabric.sdk.aberic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    /** 智能合约版本号 */
    private String chaincodeVersion; // 1.0
    /** 指定ID的智能合约 */
    private ChaincodeID chaincodeID;
    /** 单个提案请求的超时时间以毫秒为单位 */
    private int proposalWaitTime = 200000;
    /** 事务等待时间以秒为单位 */
    private int transactionWaitTime = 120;

    /** 部署等待时间以秒为单位 */
//    private int deployWatiTime = 120000;

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
    Map<String, String> install(IntermediateOrg org) throws ProposalException, InvalidArgumentException {
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
    Map<String, String> instantiate(IntermediateOrg org, String[] args) throws ProposalException, InvalidArgumentException, IOException, ChaincodeEndorsementPolicyParseException, InterruptedException, ExecutionException, TimeoutException {
        /// Send transaction proposal to all peers
        InstantiateProposalRequest instantiateProposalRequest = org.getClient().newInstantiationProposalRequest();
        instantiateProposalRequest.setChaincodeID(chaincodeID);
        instantiateProposalRequest.setProposalWaitTime(proposalWaitTime);
        instantiateProposalRequest.setArgs(args);

        ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
        chaincodeEndorsementPolicy.fromYamlFile(new File("/code/src/policy/chaincodeendorsementpolicy.yaml"));
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
    Map<String, String> upgrade(IntermediateOrg org, String[] args) throws ProposalException, InvalidArgumentException, IOException, ChaincodeEndorsementPolicyParseException, InterruptedException, ExecutionException, TimeoutException {
        /// Send transaction proposal to all peers
        UpgradeProposalRequest upgradeProposalRequest = org.getClient().newUpgradeProposalRequest();
        upgradeProposalRequest.setChaincodeID(chaincodeID);
        upgradeProposalRequest.setProposalWaitTime(proposalWaitTime);
        upgradeProposalRequest.setArgs(args);

        ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
        chaincodeEndorsementPolicy.fromYamlFile(new File("/code/src/policy/chaincodeendorsementpolicy.yaml"));
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
    Map<String, String> invoke(IntermediateOrg org, String fcn, String[] args) throws InvalidArgumentException, ProposalException, IOException, InterruptedException, ExecutionException, TimeoutException {
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
     * @param org  中继组织对象
     * @param fcn  方法名
     * @param args 参数数组
     */
    Map<String, String> query(IntermediateOrg org, String fcn, String[] args) throws InvalidArgumentException, ProposalException {
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
    private Map<String, String> toOrdererResponse(Collection<ProposalResponse> proposalResponses, IntermediateOrg org) throws InvalidArgumentException, UnsupportedEncodingException, InterruptedException, ExecutionException, TimeoutException {
        Map<String, String> resultMap = new HashMap<>();
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
            ProposalResponse firstTransactionProposalResponse = failed.iterator().next();
            log.error("Not enough endorsers for inspect:" + failed.size() + " endorser error: " + firstTransactionProposalResponse.getMessage() + ". Was verified: "
                    + firstTransactionProposalResponse.isVerified());
            resultMap.put("code", "error");
            resultMap.put("data", firstTransactionProposalResponse.getMessage());
            return resultMap;
        } else {
            log.info("Successfully received transaction proposal responses.");
            ProposalResponse resp = proposalResponses.iterator().next();
            log.debug("TransactionID: " + resp.getTransactionID());
            byte[] x = resp.getChaincodeActionResponsePayload();
            String resultAsString = null;
            if (x != null) {
                resultAsString = new String(x, "UTF-8");
            }
            log.info("resultAsString = " + resultAsString);
            org.getChannel().get().sendTransaction(successful).get(transactionWaitTime, TimeUnit.SECONDS);
            resultMap.put("code", "success");
            resultMap.put("data", resultAsString);
            resultMap.put("txid", resp.getTransactionID());
            return resultMap;
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
    private Map<String, String> toPeerResponse(Collection<ProposalResponse> proposalResponses, boolean checkVerified) {
        Map<String, String> resultMap = new HashMap<>();
        for (ProposalResponse proposalResponse : proposalResponses) {
            if ((checkVerified && !proposalResponse.isVerified()) || proposalResponse.getStatus() != ProposalResponse.Status.SUCCESS) {
                String data = String.format("Failed install/query proposal from peer %s status: %s. Messages: %s. Was verified : %s",
                        proposalResponse.getPeer().getName(), proposalResponse.getStatus(), proposalResponse.getMessage(), proposalResponse.isVerified());
                log.debug(data);
                resultMap.put("code", "error");
                resultMap.put("data", data);
            } else {
                String payload = proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
                log.debug("Install/Query payload from peer: " + proposalResponse.getPeer().getName());
                log.debug("TransactionID: " + proposalResponse.getTransactionID());
                log.debug("" + payload);
                resultMap.put("code", "success");
                resultMap.put("data", payload);
                resultMap.put("txid", proposalResponse.getTransactionID());
            }
        }
        return resultMap;
    }

    /**
     * 设置单个提案请求的超时时间以毫秒为单位
     *
     * @param proposalWaitTime 超时时间以毫秒为单位
     */
    void setProposalWaitTime(int proposalWaitTime) {
        this.proposalWaitTime = proposalWaitTime;
    }

    /**
     * 设置事务等待时间以秒为单位
     *
     * @param invokeWaitTime 事务等待时间以秒为单位
     */
    void setTransactionWaitTime(int invokeWaitTime) {
        this.transactionWaitTime = invokeWaitTime;
    }

}
