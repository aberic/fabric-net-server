package org.hyperledger.fabric.sdk.aberic;

import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.helper.Utils;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 描述：组织生成器
 *
 * @author : Aberic 【2018/5/4 15:59】
 */
public class OrgManager {

    private Map<String, IntermediateOrg> orgMap;
    private String orgHash;

    public OrgManager() {
        orgMap = new LinkedHashMap<>();
    }

    /**
     * 初始化组织名称，该对象的必须首次调用方法
     *
     * @param orgHash   组织Hash
     * @param openTLS   设置是否开启TLS
     *
     * @return self
     */
    public OrgManager init(String orgHash, boolean openTLS) {
        this.orgHash = orgHash;
        if (orgMap.get(orgHash) != null) {
            throw new RuntimeException(String.format("OrgManager had the same id of %s", orgHash));
        } else {
            orgMap.put(orgHash, new IntermediateOrg());
        }
        orgMap.get(orgHash).openTLS(openTLS);
        return this;
    }

    /**
     * 设置默认用户（一个特殊用户，即可对Channel及ChainCode进行操作的用户，一般为Admin，在有特殊操作需求的情况下，channelArtifactsPath不为null；
     * 也可以是一个已经在服务器生成好用户相关证书文件的用户，在没有特殊操作需求的情况下，一般channelArtifactsPath设置为null即可）
     *
     * @param username             用户名
     * @param cryptoConfigPath     用户/节点组织/排序服务证书文件路径
     *
     * @return self
     */
    public OrgManager setUser(@Nonnull String username, @Nonnull String cryptoConfigPath) {
        orgMap.get(orgHash).setUsername(username);
        orgMap.get(orgHash).setCryptoConfigPath(cryptoConfigPath);
        return this;
    }

    public OrgManager setOrderers(String ordererDomainName) {
        orgMap.get(orgHash).setOrdererDomainName(ordererDomainName);
        return this;
    }

    public OrgManager addOrderer(String name, String location) {
        orgMap.get(orgHash).addOrderer(name, location);
        return this;
    }

    public OrgManager setPeers(String orgName, String orgMSPID, String orgDomainName) {
        orgMap.get(orgHash).setOrgName(orgName);
        orgMap.get(orgHash).setOrgMSPID(orgMSPID);
        orgMap.get(orgHash).setOrgDomainName(orgDomainName);
        return this;
    }

    public OrgManager addPeer(String peerName, String peerEventHubName, String peerLocation, String peerEventHubLocation, boolean isEventListener) {
        orgMap.get(orgHash).addPeer(peerName, peerEventHubName, peerLocation, peerEventHubLocation, isEventListener);
        return this;
    }

    /**
     * 设置智能合约
     *
     * @param chaincodeName    智能合约名称
     * @param chaincodePath    智能合约路径
     * @param chaincodeVersion 智能合约版本
     * @param proposalWaitTime 单个提案请求的超时时间以毫秒为单位
     * @param invokeWaitTime   事务等待时间以秒为单位
     *
     * @return Fabric
     */
    public OrgManager setChainCode(String chaincodeName, String chaincodePath, String chaincodeVersion, int proposalWaitTime, int invokeWaitTime) {
        IntermediateChaincodeID chaincode = new IntermediateChaincodeID();
        chaincode.setChaincodeName(chaincodeName);
        chaincode.setChaincodePath(chaincodePath);
        chaincode.setChaincodeVersion(chaincodeVersion);
        chaincode.setProposalWaitTime(proposalWaitTime);
        chaincode.setTransactionWaitTime(invokeWaitTime);
        orgMap.get(orgHash).setChainCode(chaincode);
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
        orgMap.get(orgHash).setChannel(channel);
        return this;
    }

    /**
     * 设置监听事件
     *
     * @param blockListener BlockListener
     */
    public OrgManager setBlockListener(BlockListener blockListener) {
        orgMap.get(orgHash).setBlockListener(blockListener);
        return this;
    }

    public void add() {
        if (orgMap.get(orgHash).getPeers().size() == 0) {
            throw new RuntimeException("peers is null or peers size is 0");
        }
        if (orgMap.get(orgHash).getOrderers().size() == 0) {
            throw new RuntimeException("orderers is null or orderers size is 0");
        }
        if (orgMap.get(orgHash).getChainCode() == null) {
            throw new RuntimeException("chaincode must be instantiated");
        }

        // 根据TLS开启状态循环确认Peer节点各服务的请求grpc协议
        for (int i = 0; i < orgMap.get(orgHash).getPeers().size(); i++) {
            orgMap.get(orgHash).getPeers().get(i).setPeerLocation(grpcTLSify(orgMap.get(orgHash).openTLS(), orgMap.get(orgHash).getPeers().get(i).getPeerLocation()));
            orgMap.get(orgHash).getPeers().get(i).setPeerEventHubLocation(grpcTLSify(orgMap.get(orgHash).openTLS(), orgMap.get(orgHash).getPeers().get(i).getPeerEventHubLocation()));
        }
        // 根据TLS开启状态循环确认Orderer节点各服务的请求grpc协议
        for (int i = 0; i < orgMap.get(orgHash).getOrderers().size(); i++) {
            orgMap.get(orgHash).getOrderers().get(i).setOrdererLocation(grpcTLSify(orgMap.get(orgHash).openTLS(), orgMap.get(orgHash).getOrderers().get(i).getOrdererLocation()));
        }
    }

    public FabricManager use(String orgHash) throws Exception {
        IntermediateOrg org = orgMap.get(orgHash);
        // java.io.tmpdir : C:\Users\aberic\AppData\Local\Temp\
        File storeFile = new File(String.format("%s/HFCStore%s.properties", System.getProperty("java.io.tmpdir"), orgHash));
        FabricStore fabricStore = new FabricStore(storeFile);
        org.init(fabricStore);
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
