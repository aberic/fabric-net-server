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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
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

    /** orderer 排序服务器所在根域名，如：example.com */
    private String ordererDomainName;
    /** orderer 排序服务器集合 */
    private List<IntermediateOrderer> orderers = new LinkedList<>();

    /** 当前指定的组织名称，如：Org1 */
    private String orgName;
    /** 当前指定的组织名称，如：Org1MSP */
    private String orgMSPID;
    /** 当前指定的组织所在根域名，如：org1.example.com */
    private String orgDomainName;
    /** orderer 排序服务器集合 */
    private List<IntermediatePeer> peers = new LinkedList<>();

    /** 是否开启TLS访问 */
    private boolean openTLS;

    /** 频道对象 */
    private IntermediateChannel channel;

    /** 智能合约对象 */
    private IntermediateChaincodeID chaincode;
    /** 事件监听 */
    private BlockListener blockListener;

    /** channel-artifacts所在路径 */
    private String channelArtifactsPath;
    /** crypto-config所在路径 */
    private String cryptoConfigPath;

    private HFClient client;

    private Map<String, User> userMap = new HashMap<>();

    void init(FabricStore fabricStore) throws Exception {
        setPeerAdmin(fabricStore);
    }

    private void setPeerAdmin(FabricStore fabricStore) throws IOException {
        File skFile = Paths.get(cryptoConfigPath, "/peerOrganizations/", orgDomainName, String.format("/users/%s@%s/msp/keystore", "Admin", orgDomainName)).toFile();
        File certificateFile = Paths.get(cryptoConfigPath, "/peerOrganizations/", getOrgDomainName(),
                String.format("/users/%s@%s/msp/signcerts/%s@%s-cert.pem", "Admin", orgDomainName, "Admin", orgDomainName)).toFile();
        log.debug("skFile = " + skFile.getAbsolutePath());
        log.debug("certificateFile = " + certificateFile.getAbsolutePath());
        // 一个特殊的用户，可以创建通道，连接对等点，并安装链码
        addUser(fabricStore.getMember(username, orgName, orgMSPID, findFileSk(skFile), certificateFile));
    }

    /**
     * 设置CA默认请求用户名或指定的带密码参数的请求用户名
     *
     * @param username 用户名
     */
    void setUsername(String username) {
        this.username = username;
    }

    String getOrdererDomainName() {
        return ordererDomainName;
    }

    void setOrdererDomainName(String ordererDomainName) {
        this.ordererDomainName = ordererDomainName;
    }

    /** 新增排序服务器 */
    void addOrderer(String name, String location) {
        orderers.add(new IntermediateOrderer(name, location));
    }

    /** 获取排序服务器集合 */
    List<IntermediateOrderer> getOrderers() {
        return orderers;
    }

    void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * 设置会员id信息并将用户状态更新至存储配置对象
     *
     * @param orgMSPID 会员id
     */
    void setOrgMSPID(String orgMSPID) {
        this.orgMSPID = orgMSPID;
    }

    String getOrgDomainName() {
        return orgDomainName;
    }

    void setOrgDomainName(String orgDomainName) {
        this.orgDomainName = orgDomainName;
    }

    /** 新增节点服务器 */
    void addPeer(String peerName, String peerEventHubName, String peerLocation, String peerEventHubLocation, boolean isEventListener) {
        peers.add(new IntermediatePeer(peerName, peerEventHubName, peerLocation, peerEventHubLocation, isEventListener));
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

    void setCryptoConfigPath(String cryptoConfigPath) {
        this.cryptoConfigPath = cryptoConfigPath;
    }

    String getCryptoConfigPath() {
        return cryptoConfigPath;
    }

    void setBlockListener(BlockListener blockListener) {
        this.blockListener = blockListener;
    }

    BlockListener getBlockListener() {
        return blockListener;
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

    private void addUser(IntermediateUser user) {
        userMap.put(user.getName(), user);
    }

    User getUser() {
        return userMap.get(username);
    }

    void setClient(HFClient client) throws CryptoException, InvalidArgumentException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.client = client;
        log.debug("Create instance of HFClient");
        client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        log.debug("Set Crypto Suite of HFClient");
    }

    HFClient getClient() {
        return client;
    }

    /**
     * 从指定路径中获取后缀为 _sk 的文件，且该路径下有且仅有该文件
     *
     * @param directory 指定路径
     * @return File
     */
    private File findFileSk(File directory) {
        File[] matches = directory.listFiles((dir, name) -> name.endsWith("_sk"));
        if (null == matches) {
            throw new RuntimeException(String.format("Matches returned null does %s directory exist?", directory.getAbsoluteFile().getName()));
        }
        if (matches.length != 1) {
            throw new RuntimeException(String.format("Expected in %s only 1 sk file but found %d", directory.getAbsoluteFile().getName(), matches.length));
        }
        return matches[0];
    }
}