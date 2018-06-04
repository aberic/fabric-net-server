package org.hyperledger.fabric.sdk.aberic;

import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 描述：
 *
 * @author : Aberic 【2018/5/4 16:43】
 */
public class FabricManager {

    private IntermediateOrg org;

    FabricManager(IntermediateOrg org) {
        this.org = org;
    }

    /** 安装智能合约 */
    public Map<String, String> install() throws ProposalException, InvalidArgumentException {
        return org.getChainCode().install(org);
    }

    /**
     * 实例化智能合约
     *
     * @param args 初始化参数数组
     */
    public Map<String, String> instantiate(String[] args) throws ProposalException, InvalidArgumentException, IOException, ChaincodeEndorsementPolicyParseException, InterruptedException, ExecutionException, TimeoutException {
        return org.getChainCode().instantiate(org, args);
    }

    /**
     * 升级智能合约
     *
     * @param args 初始化参数数组
     */
    public Map<String, String> upgrade(String[] args) throws ProposalException, InvalidArgumentException, IOException, ChaincodeEndorsementPolicyParseException, InterruptedException, ExecutionException, TimeoutException {
        return org.getChainCode().upgrade(org, args);
    }

    /**
     * 执行智能合约
     *
     * @param fcn  方法名
     * @param args 参数数组
     */
    public Map<String, String> invoke(String fcn, String[] args) throws InvalidArgumentException, ProposalException, IOException, InterruptedException, ExecutionException, TimeoutException {
        return org.getChainCode().invoke(org, fcn, args);
    }

    /**
     * 查询智能合约
     *
     * @param fcn  方法名
     * @param args 参数数组
     */
    public Map<String, String> query(String fcn, String[] args) throws InvalidArgumentException, ProposalException {
        return org.getChainCode().query(org, fcn, args);
    }

    /**
     * 在指定频道内根据transactionID查询区块
     *
     * @param txID transactionID
     */
    public Map<String, String> queryBlockByTransactionID(String txID) throws ProposalException, IOException, InvalidArgumentException {
        return org.getChannel().queryBlockByTransactionID(txID);
    }

    /**
     * 在指定频道内根据hash查询区块
     *
     * @param blockHash hash
     */
    public Map<String, String> queryBlockByHash(byte[] blockHash) throws ProposalException, IOException, InvalidArgumentException {
        return org.getChannel().queryBlockByHash(blockHash);
    }

    /**
     * 在指定频道内根据区块高度查询区块
     *
     * @param blockNumber 区块高度
     */
    public Map<String, String> queryBlockByNumber(long blockNumber) throws ProposalException, IOException, InvalidArgumentException {
        return org.getChannel().queryBlockByNumber(blockNumber);
    }

    /**
     * Peer加入频道
     *
     * @param peerName             当前指定的组织节点域名
     * @param peerEventHubName     当前指定的组织节点事件域名
     * @param peerLocation         当前指定的组织节点访问地址
     * @param peerEventHubLocation 当前指定的组织节点事件监听访问地址
     * @param isEventListener      当前peer是否增加Event事件处理
     */
    public Map<String, String> joinPeer(String peerName, String peerEventHubName, String peerLocation, String peerEventHubLocation, boolean isEventListener) throws ProposalException, InvalidArgumentException {
        return org.getChannel().joinPeer(new IntermediatePeer(peerName, peerEventHubName, peerLocation, peerEventHubLocation, isEventListener));
    }

    /** 查询当前频道的链信息，包括链长度、当前最新区块hash以及当前最新区块的上一区块hash */
    public Map<String, String> getBlockchainInfo() throws ProposalException, InvalidArgumentException {
        return org.getChannel().getBlockchainInfo();
    }

}
