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

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.StringUtils;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;

import java.io.IOException;

/**
 * 描述：区块链网络服务管理器
 *
 * @author : Aberic 【2018/5/4 16:43】
 */
public class FabricManager {

    private IntermediateOrg org;

    FabricManager(IntermediateOrg org) {
        this.org = org;
    }

    public void setUser(String leagueName, String orgName, String peerName, String username, String skPath, String certificatePath) throws InvalidArgumentException {
        if (StringUtils.equals(username, org.getUsername())) {
            return;
        }
        User user = org.getUser(username);
        if (null == user) {
            IntermediateUser intermediateUser = new IntermediateUser(leagueName, orgName, peerName, username, skPath, certificatePath);
            org.setUsername(username);
            org.addUser(leagueName, orgName, peerName, intermediateUser, org.getFabricStore());
        }
        org.getClient().setUserContext(org.getUser(username));
    }

    /** 安装智能合约 */
    public JSONObject install() throws ProposalException, InvalidArgumentException, TransactionException {
        return org.getChainCode().install(org);
    }

    /**
     * 实例化智能合约
     *
     * @param args 初始化参数数组
     */
    public JSONObject instantiate(String[] args) throws ProposalException, InvalidArgumentException, IOException, ChaincodeEndorsementPolicyParseException, TransactionException {
        return org.getChainCode().instantiate(org, args);
    }

    /**
     * 升级智能合约
     *
     * @param args 初始化参数数组
     */
    public JSONObject upgrade(String[] args) throws ProposalException, InvalidArgumentException, IOException, ChaincodeEndorsementPolicyParseException, TransactionException {
        return org.getChainCode().upgrade(org, args);
    }

    /**
     * 执行智能合约
     *
     * @param fcn  方法名
     * @param args 参数数组
     */
    public JSONObject invoke(String fcn, String[] args) throws InvalidArgumentException, ProposalException, IOException, TransactionException {
        return org.getChainCode().invoke(org, fcn, args);
    }

    /**
     * 查询智能合约
     *
     * @param fcn  方法名
     * @param args 参数数组
     */
    public JSONObject query(String fcn, String[] args) throws InvalidArgumentException, ProposalException, TransactionException {
        return org.getChainCode().query(org, fcn, args);
    }

    /**
     * 在指定频道内根据transactionID查询区块
     *
     * @param txID transactionID
     */
    public JSONObject queryBlockByTransactionID(String txID) throws ProposalException, IOException, InvalidArgumentException, TransactionException {
        return org.getChannel().queryBlockByTransactionID(txID);
    }

    /**
     * 在指定频道内根据hash查询区块
     *
     * @param blockHash hash
     */
    public JSONObject queryBlockByHash(byte[] blockHash) throws ProposalException, IOException, InvalidArgumentException, TransactionException {
        return org.getChannel().queryBlockByHash(blockHash);
    }

    /**
     * 在指定频道内根据区块高度查询区块
     *
     * @param blockNumber 区块高度
     */
    public JSONObject queryBlockByNumber(long blockNumber) throws ProposalException, IOException, InvalidArgumentException, TransactionException {
        return org.getChannel().queryBlockByNumber(blockNumber);
    }

    /** Peer加入频道 */
    public JSONObject joinPeer() throws ProposalException, InvalidArgumentException {
        return org.getChannel().joinPeer(org.getPeers().get(0));
    }

    /** 查询当前频道的链信息，包括链长度、当前最新区块hash以及当前最新区块的上一区块hash */
    public JSONObject queryBlockChainInfo() throws ProposalException, InvalidArgumentException, TransactionException {
        return org.getChannel().queryBlockChainInfo();
    }

}
