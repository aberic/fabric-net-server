package org.hyperledger.fabric.sdk.aberic;

import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.helper.Utils;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 描述：组织生成器
 *
 * @author : Aberic 【2018/5/4 15:59】
 */
public class OrgManager {

    private Map<String, IntermediateOrg> orgMap;
    private String orgName;

    public OrgManager() {
        orgMap = new LinkedHashMap<>();
    }

    /**
     * 初始化组织名称，该对象的必须首次调用方法
     *
     * @param orgName 组织名称
     * @return self
     */
    public OrgManager init(String orgName) {
        this.orgName = orgName;
        if (orgMap.get(orgName) != null) {
            throw new RuntimeException(String.format("OrgManager had the same name of %s", orgName));
        } else {
            orgMap.put(orgName, new IntermediateOrg());
        }
        return this;
    }

    /**
     * 设置CA请求的Http协议URL
     *
     * @param caName     CA名称
     * @param caLocation CA请求URL
     * @return self
     */
    public OrgManager setCA(String caName, String caLocation) {
        orgMap.get(orgName).setCaName(caName);
        orgMap.get(orgName).setCALocation(caLocation);
        return this;
    }

    /**
     * 设置默认用户（一个特殊用户，即可对Channel及ChainCode进行操作的用户，一般为Admin，在有特殊操作需求的情况下，channelArtifactsPath不为null；
     * 也可以是一个已经在服务器生成好用户相关证书文件的用户，在没有特殊操作需求的情况下，一般channelArtifactsPath设置为null即可）
     *
     * @param username             用户名
     * @param cryptoConfigPath     用户/节点组织/排序服务证书文件路径
     * @param channelArtifactsPath 联盟相关证书文件路径
     * @return self
     */
    public OrgManager setUser(@Nonnull String username, @Nonnull String cryptoConfigPath, String channelArtifactsPath) {
        orgMap.get(orgName).setUsername(username);
        orgMap.get(orgName).setCryptoConfigPath(cryptoConfigPath);
        orgMap.get(orgName).setChannelArtifactsPath(channelArtifactsPath);
        return this;
    }

    /**
     * 设置已注册用户
     *
     * @param username         用户名
     * @param password         密码
     * @param affiliation      所属组织关系
     * @param roles            角色
     * @param cryptoConfigPath 用户/节点组织/排序服务证书文件路径
     * @return self
     */
    public OrgManager setUser(@Nonnull String username, @Nonnull String password, String affiliation, Set<String> roles, @Nonnull String cryptoConfigPath) {
        orgMap.get(orgName).setUsername(username);
        orgMap.get(orgName).setPassword(password);
        orgMap.get(orgName).setAffiliation(affiliation);
        orgMap.get(orgName).setRoles(roles);
        orgMap.get(orgName).setCryptoConfigPath(cryptoConfigPath);
        return this;
    }

    public OrgManager setOrderers(String ordererDomainName) {
        orgMap.get(orgName).setOrdererDomainName(ordererDomainName);
        return this;
    }

    public OrgManager addOrderer(String name, String location) {
        orgMap.get(orgName).addOrderer(name, location);
        return this;
    }

    public OrgManager setPeers(String orgMSPID, String orgDomainName) {
        orgMap.get(orgName).setOrgName(orgName);
        orgMap.get(orgName).setOrgMSPID(orgMSPID);
        orgMap.get(orgName).setOrgDomainName(orgDomainName);
        return this;
    }

    public OrgManager addPeer(String peerName, String peerEventHubName, String peerLocation, String peerEventHubLocation, boolean isEventListener) {
        orgMap.get(orgName).addPeer(peerName, peerEventHubName, peerLocation, peerEventHubLocation, isEventListener);
        return this;
    }

    /**
     * 设置智能合约
     *
     * @param chaincodeName    智能合约名称
     * @param chaincodeSource  可能是包含智能合约的go环境路径
     * @param chaincodePath    智能合约路径
     * @param chaincodeVersion 智能合约版本
     * @param proposalWaitTime 单个提案请求的超时时间以毫秒为单位
     * @param invokeWaitTime   事务等待时间以秒为单位
     * @return Fabric
     */
    public OrgManager setChainCode(String chaincodeName, String chaincodeSource, String chaincodePath, String chaincodeVersion, int proposalWaitTime, int invokeWaitTime) {
        IntermediateChaincodeID chaincode = new IntermediateChaincodeID();
        chaincode.setChaincodeName(chaincodeName);
        chaincode.setChaincodeSource(chaincodeSource);
        chaincode.setChaincodePath(chaincodePath);
        chaincode.setChaincodeVersion(chaincodeVersion);
        chaincode.setProposalWaitTime(proposalWaitTime);
        chaincode.setTransactionWaitTime(invokeWaitTime);
        orgMap.get(orgName).setChainCode(chaincode);
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
        orgMap.get(orgName).setChannel(channel);
        return this;
    }

    /**
     * 设置是否开启TLS
     *
     * @param openTLS 是否
     */
    public OrgManager openTLS(boolean openTLS) {
        orgMap.get(orgName).openTLS(openTLS);
        return this;
    }

    /**
     * 设置是否开启CATLS
     *
     * @param openCATLS 是否
     */
    public OrgManager openCATLS(boolean openCATLS) {
        orgMap.get(orgName).openCATLS(openCATLS);
        return this;
    }

    /**
     * 设置监听事件
     *
     * @param blockListener BlockListener
     */
    public OrgManager setBlockListener(BlockListener blockListener) {
        orgMap.get(orgName).setBlockListener(blockListener);
        return this;
    }

    public void add() {
        if (orgMap.get(orgName).getPeers().size() == 0) {
            throw new RuntimeException("peers is null or peers size is 0");
        }
        if (orgMap.get(orgName).getOrderers().size() == 0) {
            throw new RuntimeException("orderers is null or orderers size is 0");
        }
        if (orgMap.get(orgName).getChainCode() == null) {
            throw new RuntimeException("chaincode must be instantiated");
        }

        // 根据TLS开启状态循环确认Peer节点各服务的请求grpc协议
        for (int i = 0; i < orgMap.get(orgName).getPeers().size(); i++) {
            orgMap.get(orgName).getPeers().get(i).setPeerLocation(grpcTLSify(orgMap.get(orgName).openTLS(), orgMap.get(orgName).getPeers().get(i).getPeerLocation()));
            orgMap.get(orgName).getPeers().get(i).setPeerEventHubLocation(grpcTLSify(orgMap.get(orgName).openTLS(), orgMap.get(orgName).getPeers().get(i).getPeerEventHubLocation()));
        }
        // 根据TLS开启状态循环确认Orderer节点各服务的请求grpc协议
        for (int i = 0; i < orgMap.get(orgName).getOrderers().size(); i++) {
            orgMap.get(orgName).getOrderers().get(i).setOrdererLocation(grpcTLSify(orgMap.get(orgName).openTLS(), orgMap.get(orgName).getOrderers().get(i).getOrdererLocation()));
        }
        // 根据CATLS开启状态循环确认CA服务的请求http协议
        orgMap.get(orgName).setCALocation(httpTLSify(orgMap.get(orgName).openCATLS(), orgMap.get(orgName).getCALocation()));
    }

    public FabricManager use(String orgName) throws Exception {
        IntermediateOrg org = orgMap.get(orgName);
        // java.io.tmpdir : C:\Users\aberic\AppData\Local\Temp\
        File storeFile = new File(System.getProperty("java.io.tmpdir") + "/HFCStore" + orgName + ".properties");
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
