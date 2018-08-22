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

import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.helper.Utils;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 描述：组织生成器
 *
 * @author : Aberic 【2018/5/4 15:59】
 */
public class OrgManager {

    private Map<String, IntermediateOrg> orgMap;
    private FabricStore fabricStore;
    private String cc;

    public OrgManager() {
        orgMap = new LinkedHashMap<>();
        // java.io.tmpdir : C:\Users\aberic\AppData\Local\Temp\
        File storeFile = new File(String.format("%s/HFCStore.properties", System.getProperty("java.io.tmpdir")));
        fabricStore = new FabricStore(storeFile);
    }

    /**
     * 初始化组织名称，该对象的必须首次调用方法
     *
     * @param cc 组织Hash
     * @param openTLS     设置是否开启TLS
     *
     * @return self
     */
    public OrgManager init(String cc, String orgMSPID, boolean openTLS) {
        this.cc = cc;
        if (orgMap.get(cc) != null) {
            throw new RuntimeException(String.format("OrgManager had the same cc of %s", cc));
        } else {
            orgMap.put(cc, new IntermediateOrg());
        }
        orgMap.get(cc).setOrgMSPID(orgMSPID);
        orgMap.get(cc).openTLS(openTLS);
        orgMap.get(cc).setFabricStore(fabricStore);
        return this;
    }

    /**
     * 设置默认用户（一个特殊用户，即可对Channel及ChainCode进行操作的用户，一般为Admin，在有特殊操作需求的情况下，channelArtifactsPath不为null；
     * 也可以是一个已经在服务器生成好用户相关证书文件的用户，在没有特殊操作需求的情况下，一般channelArtifactsPath设置为null即可）
     *
     * @param username         用户名
     * @param skPath          带有节点签名密钥的PEM文件——sk路径
     * @param certificatePath 带有节点的X.509证书的PEM文件——certificate路径
     *
     * @return self
     */
    public OrgManager setUser(@Nonnull String leagueName, @Nonnull String orgName, @Nonnull String peerName, @Nonnull String username, @Nonnull String skPath, @Nonnull String certificatePath) {
        IntermediateUser user = new IntermediateUser(leagueName, orgName, peerName, username, skPath, certificatePath);
        orgMap.get(cc).addUser(leagueName, orgName, peerName, user, fabricStore);
        return this;
    }

    public void addOrderer(String name, String location, String serverCrtPath, String clientCertPath, String clientKeyPath) {
        orgMap.get(cc).addOrderer(name, String.format("%s%s", "grpc://", location), serverCrtPath, clientCertPath, clientKeyPath);
    }

    public void addPeer(String peerName, String peerLocation, String peerEventHubLocation, String serverCrtPath, String clientCertPath, String clientKeyPath) {
        orgMap.get(cc).addPeer(peerName, String.format("%s%s", "grpc://", peerLocation), String.format("%s%s", "grpc://", peerEventHubLocation), serverCrtPath, clientCertPath, clientKeyPath);
    }

    /**
     * 设置智能合约
     *
     * @param chaincodeName    智能合约名称
     * @param chaincodePath    智能合约路径
     * @param chaincodeSource  智能合约安装路径所在路径
     * @param chaincodePolicy  智能合约背书文件路径
     * @param chaincodeVersion 智能合约版本
     * @param proposalWaitTime 单个提案请求的超时时间以毫秒为单位
     */
    public OrgManager setChainCode(String chaincodeName, String chaincodePath, String chaincodeSource, String chaincodePolicy, String chaincodeVersion, int proposalWaitTime) {
        IntermediateChaincodeID chaincode = new IntermediateChaincodeID();
        chaincode.setChaincodeName(chaincodeName);
        chaincode.setChaincodeSource(chaincodeSource);
        chaincode.setChaincodePath(chaincodePath);
        chaincode.setChaincodePolicy(chaincodePolicy);
        chaincode.setChaincodeVersion(chaincodeVersion);
        chaincode.setProposalWaitTime(proposalWaitTime);
        orgMap.get(cc).setChainCode(chaincode);
        return this;
    }

    /**
     * 设置频道
     *
     * @param channelName 频道名称
     *
     * @return Fabric
     */
    public OrgManager setChannel(String channelName) {
        IntermediateChannel channel = new IntermediateChannel();
        channel.setChannelName(channelName);
        orgMap.get(cc).setChannel(channel);
        return this;
    }

    /**
     * 设置监听事件
     *
     * @param blockListener BlockListener
     */
    public void setBlockListener(BlockListener blockListener) {
        orgMap.get(cc).setBlockListener(blockListener);
    }

    public void setChaincodeEventListener(String eventNames, ChaincodeEventListener chaincodeEventListener) {
        orgMap.get(cc).setChaincodeEventListener(eventNames, chaincodeEventListener);
    }

    public void add() {
        if (orgMap.get(cc).getPeers().size() == 0) {
            throw new RuntimeException("peers is null or peers size is 0");
        }
        if (orgMap.get(cc).getOrderers().size() == 0) {
            throw new RuntimeException("orderers is null or orderers size is 0");
        }
        if (orgMap.get(cc).getChainCode() == null) {
            throw new RuntimeException("chaincode must be instantiated");
        }

        // 根据TLS开启状态循环确认Peer节点各服务的请求grpc协议
        for (int i = 0; i < orgMap.get(cc).getPeers().size(); i++) {
            orgMap.get(cc).getPeers().get(i).setPeerLocation(grpcTLSify(orgMap.get(cc).openTLS(), orgMap.get(cc).getPeers().get(i).getPeerLocation()));
            orgMap.get(cc).getPeers().get(i).setPeerEventHubLocation(grpcTLSify(orgMap.get(cc).openTLS(), orgMap.get(cc).getPeers().get(i).getPeerEventHubLocation()));
        }
        // 根据TLS开启状态循环确认Orderer节点各服务的请求grpc协议
        for (int i = 0; i < orgMap.get(cc).getOrderers().size(); i++) {
            orgMap.get(cc).getOrderers().get(i).setOrdererLocation(grpcTLSify(orgMap.get(cc).openTLS(), orgMap.get(cc).getOrderers().get(i).getOrdererLocation()));
        }
    }

    public FabricManager use(String cc, String username) throws Exception {
        IntermediateOrg org = orgMap.get(cc);
//        org.init(fabricStore);
        org.setUsername(username);
        org.setClient(HFClient.createNewInstance());
        org.getChannel().init(org);
        return new FabricManager(org);
    }

    private String grpcTLSify(boolean openTLS, String location) {
        location = location.trim();
        Exception e = Utils.checkGrpcUrl(location);
        if (e != null) {
            throw new RuntimeException(String.format("Bad TEST parameters for grpc url %s", location), e);
        }
        return openTLS ? location.replaceFirst("^grpc://", "grpcs://") : location;

    }

    private String httpTLSify(boolean openCATLS, String location) {
        location = location.trim();
        return openCATLS ? location.replaceFirst("^http://", "https://") : location;
    }

}
