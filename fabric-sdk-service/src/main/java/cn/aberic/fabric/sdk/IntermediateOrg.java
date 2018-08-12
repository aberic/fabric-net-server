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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 描述：中继组织对象
 *
 * @author : Aberic 【2018/5/4 15:32】
 */
class IntermediateOrg {

    private Logger log = LogManager.getLogger(IntermediateOrg.class);
    /** 执行SDK的Fabric用户名 */
    private String username;
    /** orderer 排序服务器集合 */
    private List<IntermediateOrderer> orderers = new LinkedList<>();
    /** 当前指定的组织名称，如：Org1MSP */
    private String orgMSPID;
    /** orderer 排序服务器集合 */
    private List<IntermediatePeer> peers = new LinkedList<>();
    /** 是否开启TLS访问 */
    private boolean openTLS;
    /** 频道对象 */
    private IntermediateChannel channel;
    /** 智能合约对象 */
    private IntermediateChaincodeID chaincode;
    private String eventNames;
    /** 事件监听 */
    private BlockListener blockListener;
    private ChaincodeEventListener chaincodeEventListener;
    private HFClient client;
    private Map<String, User> userMap = new HashMap<>();
    private FabricStore fabricStore;

    FabricStore getFabricStore() {
        return fabricStore;
    }

    void setFabricStore(FabricStore fabricStore) {
        this.fabricStore = fabricStore;
    }

    /**
     * 设置CA默认请求用户名或指定的带密码参数的请求用户名
     *
     * @param username 用户名
     */
    void setUsername(String username) {
        this.username = username;
    }

    String getUsername() {
        return username;
    }

    /** 新增排序服务器 */
    void addOrderer(String name, String location, String serverCrtPath) {
        orderers.add(new IntermediateOrderer(name, location, serverCrtPath));
    }

    /** 获取排序服务器集合 */
    List<IntermediateOrderer> getOrderers() {
        return orderers;
    }

    /**
     * 设置会员id信息并将用户状态更新至存储配置对象
     *
     * @param orgMSPID 会员id
     */
    void setOrgMSPID(String orgMSPID) {
        this.orgMSPID = orgMSPID;
    }

    /** 新增节点服务器 */
    void addPeer(String peerName, String peerLocation, String peerEventHubLocation, String serverCrtPath) {
        peers.add(new IntermediatePeer(peerName, peerLocation, peerEventHubLocation, serverCrtPath));
    }

    /** 获取排序服务器集合 */
    List<IntermediatePeer> getPeers() {
        return peers;
    }

    void setChannel(IntermediateChannel channel) {
        this.channel = channel;
    }

    IntermediateChannel getChannel() {
        return channel;
    }

    void setChainCode(IntermediateChaincodeID chaincode) {
        this.chaincode = chaincode;
    }

    IntermediateChaincodeID getChainCode() {
        return chaincode;
    }

    void setBlockListener(BlockListener blockListener) {
        this.blockListener = blockListener;
    }

    BlockListener getBlockListener() {
        return blockListener;
    }

    void setChaincodeEventListener(String eventNames, ChaincodeEventListener chaincodeEventListener) {
        this.eventNames = eventNames;
        this.chaincodeEventListener = chaincodeEventListener;
    }

    ChaincodeEventListener getChaincodeEventListener() {
        return chaincodeEventListener;
    }

    public String getEventNames() {
        return eventNames;
    }

    /**
     * 设置是否开启TLS
     *
     * @param openTLS 是否
     */
    void openTLS(boolean openTLS) {
        this.openTLS = openTLS;
    }

    /**
     * 获取是否开启TLS
     *
     * @return 是否
     */
    boolean openTLS() {
        return openTLS;
    }

    void addUser(IntermediateUser user, FabricStore fabricStore) {
        try {
            userMap.put(user.getName(), fabricStore.getMember(user.getName(), orgMSPID, user.getSkPath(), user.getCertificatePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    User getUser(String username) {
        return userMap.get(username);
    }

    void setClient(HFClient client) throws CryptoException, InvalidArgumentException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.client = client;
        log.info("Create instance of HFClient");
        client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        log.info("Set Crypto Suite of HFClient");
    }

    HFClient getClient() {
        return client;
    }

}