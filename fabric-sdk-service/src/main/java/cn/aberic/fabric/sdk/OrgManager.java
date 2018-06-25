package cn.aberic.fabric.sdk;

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

    private Map<Integer, IntermediateOrg> orgMap;
    private int chainCodeId;

    public OrgManager() {
        orgMap = new LinkedHashMap<>();
    }

    /**
     * 初始化组织名称，该对象的必须首次调用方法
     *
     * @param chainCodeId 组织Hash
     * @param openTLS     设置是否开启TLS
     * @return self
     */
    public OrgManager init(int chainCodeId, boolean openTLS) {
        this.chainCodeId = chainCodeId;
        if (orgMap.get(chainCodeId) != null) {
            throw new RuntimeException(String.format("OrgManager had the same id of %s", chainCodeId));
        } else {
            orgMap.put(chainCodeId, new IntermediateOrg());
        }
        orgMap.get(chainCodeId).openTLS(openTLS);
        return this;
    }

    /**
     * 设置默认用户（一个特殊用户，即可对Channel及ChainCode进行操作的用户，一般为Admin，在有特殊操作需求的情况下，channelArtifactsPath不为null；
     * 也可以是一个已经在服务器生成好用户相关证书文件的用户，在没有特殊操作需求的情况下，一般channelArtifactsPath设置为null即可）
     *
     * @param username         用户名
     * @param cryptoConfigPath 用户/节点组织/排序服务证书文件路径
     * @return self
     */
    public OrgManager setUser(@Nonnull String username, @Nonnull String cryptoConfigPath) {
        orgMap.get(chainCodeId).setUsername(username);
        orgMap.get(chainCodeId).setCryptoConfigPath(cryptoConfigPath);
        return this;
    }

    public OrgManager setOrderers(String ordererDomainName) {
        orgMap.get(chainCodeId).setOrdererDomainName(ordererDomainName);
        return this;
    }

    public OrgManager addOrderer(String name, String location) {
        orgMap.get(chainCodeId).addOrderer(name, location);
        return this;
    }

    public OrgManager setPeers(String orgName, String orgMSPID, String orgDomainName) {
        orgMap.get(chainCodeId).setOrgName(orgName);
        orgMap.get(chainCodeId).setOrgMSPID(orgMSPID);
        orgMap.get(chainCodeId).setOrgDomainName(orgDomainName);
        return this;
    }

    public OrgManager addPeer(String peerName, String peerEventHubName, String peerLocation, String peerEventHubLocation, boolean isEventListener) {
        orgMap.get(chainCodeId).addPeer(peerName, peerEventHubName, peerLocation, peerEventHubLocation, isEventListener);
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
     * @return Fabric
     */
    public OrgManager setChainCode(String chaincodeName, String chaincodePath, String chaincodeSource, String chaincodeVersion, int proposalWaitTime, int invokeWaitTime) {
        IntermediateChaincodeID chaincode = new IntermediateChaincodeID();
        chaincode.setChaincodeName(chaincodeName);
        chaincode.setChaincodeSource(chaincodeSource);
        chaincode.setChaincodePath(chaincodePath);
        chaincode.setChaincodeVersion(chaincodeVersion);
        chaincode.setProposalWaitTime(proposalWaitTime);
        chaincode.setTransactionWaitTime(invokeWaitTime);
        orgMap.get(chainCodeId).setChainCode(chaincode);
        return this;
    }

    /**
     * 设置频道
     *
     * @param channelName 频道名称
     * @return Fabric
     */
    public OrgManager setChannel(String channelName) {
        IntermediateChannel channel = new IntermediateChannel();
        channel.setChannelName(channelName);
        orgMap.get(chainCodeId).setChannel(channel);
        return this;
    }

    /**
     * 设置监听事件
     *
     * @param blockListener BlockListener
     */
    public OrgManager setBlockListener(BlockListener blockListener) {
        orgMap.get(chainCodeId).setBlockListener(blockListener);
        return this;
    }

    public void add() {
        if (orgMap.get(chainCodeId).getPeers().size() == 0) {
            throw new RuntimeException("peers is null or peers size is 0");
        }
        if (orgMap.get(chainCodeId).getOrderers().size() == 0) {
            throw new RuntimeException("orderers is null or orderers size is 0");
        }
        if (orgMap.get(chainCodeId).getChainCode() == null) {
            throw new RuntimeException("chaincode must be instantiated");
        }

        // 根据TLS开启状态循环确认Peer节点各服务的请求grpc协议
        for (int i = 0; i < orgMap.get(chainCodeId).getPeers().size(); i++) {
            orgMap.get(chainCodeId).getPeers().get(i).setPeerLocation(grpcTLSify(orgMap.get(chainCodeId).openTLS(), orgMap.get(chainCodeId).getPeers().get(i).getPeerLocation()));
            orgMap.get(chainCodeId).getPeers().get(i).setPeerEventHubLocation(grpcTLSify(orgMap.get(chainCodeId).openTLS(), orgMap.get(chainCodeId).getPeers().get(i).getPeerEventHubLocation()));
        }
        // 根据TLS开启状态循环确认Orderer节点各服务的请求grpc协议
        for (int i = 0; i < orgMap.get(chainCodeId).getOrderers().size(); i++) {
            orgMap.get(chainCodeId).getOrderers().get(i).setOrdererLocation(grpcTLSify(orgMap.get(chainCodeId).openTLS(), orgMap.get(chainCodeId).getOrderers().get(i).getOrdererLocation()));
        }
    }

    public FabricManager use(int chainCodeId) throws Exception {
        IntermediateOrg org = orgMap.get(chainCodeId);
        // java.io.tmpdir : C:\Users\aberic\AppData\Local\Temp\
        File storeFile = new File(String.format("%s/HFCStore%s.properties", System.getProperty("java.io.tmpdir"), chainCodeId));
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
