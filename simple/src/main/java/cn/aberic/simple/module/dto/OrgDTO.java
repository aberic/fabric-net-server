package cn.aberic.simple.module.dto;

import cn.aberic.simple.base.BaseDTO;

/**
 * 描述：使用指定用户访问指定通道指定智能合约的组织信息
 *
 * @author : Aberic 【2018/6/5 15:51】
 */
public class OrgDTO extends BaseDTO {

    /** 组织hash */
    private String hash;
    /** 组织名称 */
    private String orgName;
    /** 是否开启TLS，0=false，1=true */
    private int tls;
    /** 是否开启CA TLS，0=false，1=true */
    private int caTls = 0;
    /** 设置默认用户 */
    private String username;
    /** CryptoConfig所在目录的目录名称 */
    private String cryptoConfigDir;
    /** ChannleArtifacts所在目录的目录名称 */
    private String channelArtifactsDir;
    /** CA名称 */
    private String caName = "ca";
    /** CA请求URL */
    private String caLocation = "http://localhost:7054";
    /** 组织唯一标识符 */
    private String orgMSPID;
    /** 组织域名 */
    private String orgDomainName;
    /** 排序服务域名 */
    private String ordererDomainName;
    /** 当前组织所访问的通道名称 */
    private String channelName;
    /** 智能合约名称 */
    private String chaincodeName;
    /** 包含智能合约的go环境路径 */
    private String chaincodeSource;
    /** 智能合约路径 */
    private String chaincodePath;
    /** 智能合约版本 */
    private String chaincodeVersion;
    /** 单个提案请求的超时时间以毫秒为单位 */
    private int proposalWaitTime;
    /** 事务等待时间以秒为单位 */
    private int invokeWaitTime;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public int isTls() {
        return tls;
    }

    public void setTls(int tls) {
        this.tls = tls;
    }

    public int isCaTls() {
        return caTls;
    }

    public void setCaTls(int caTls) {
        this.caTls = caTls;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCryptoConfigDir() {
        return cryptoConfigDir;
    }

    public void setCryptoConfigDir(String cryptoConfigDir) {
        this.cryptoConfigDir = cryptoConfigDir;
    }

    public String getChannelArtifactsDir() {
        return channelArtifactsDir;
    }

    public void setChannelArtifactsDir(String channelArtifactsDir) {
        this.channelArtifactsDir = channelArtifactsDir;
    }

    public String getCaName() {
        return caName;
    }

    public void setCaName(String caName) {
        this.caName = caName;
    }

    public String getCaLocation() {
        return caLocation;
    }

    public void setCaLocation(String caLocation) {
        this.caLocation = caLocation;
    }

    public String getOrgMSPID() {
        return orgMSPID;
    }

    public void setOrgMSPID(String orgMSPID) {
        this.orgMSPID = orgMSPID;
    }

    public String getOrgDomainName() {
        return orgDomainName;
    }

    public void setOrgDomainName(String orgDomainName) {
        this.orgDomainName = orgDomainName;
    }

    public String getOrdererDomainName() {
        return ordererDomainName;
    }

    public void setOrdererDomainName(String ordererDomainName) {
        this.ordererDomainName = ordererDomainName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChaincodeName() {
        return chaincodeName;
    }

    public void setChaincodeName(String chaincodeName) {
        this.chaincodeName = chaincodeName;
    }

    public String getChaincodeSource() {
        return chaincodeSource;
    }

    public void setChaincodeSource(String chaincodeSource) {
        this.chaincodeSource = chaincodeSource;
    }

    public String getChaincodePath() {
        return chaincodePath;
    }

    public void setChaincodePath(String chaincodePath) {
        this.chaincodePath = chaincodePath;
    }

    public String getChaincodeVersion() {
        return chaincodeVersion;
    }

    public void setChaincodeVersion(String chaincodeVersion) {
        this.chaincodeVersion = chaincodeVersion;
    }

    public int getProposalWaitTime() {
        return proposalWaitTime;
    }

    public void setProposalWaitTime(int proposalWaitTime) {
        this.proposalWaitTime = proposalWaitTime;
    }

    public int getInvokeWaitTime() {
        return invokeWaitTime;
    }

    public void setInvokeWaitTime(int invokeWaitTime) {
        this.invokeWaitTime = invokeWaitTime;
    }

    @Override
    public String toString() {
        return "OrgDTO{" +
                "hash=" + hash +
                ", orgName='" + orgName + '\'' +
                ", tls=" + tls +
                ", caTls=" + caTls +
                ", username='" + username + '\'' +
                ", cryptoConfigDir='" + cryptoConfigDir + '\'' +
                ", channelArtifactsDir='" + channelArtifactsDir + '\'' +
                ", caName='" + caName + '\'' +
                ", caLocation='" + caLocation + '\'' +
                ", orgMSPID='" + orgMSPID + '\'' +
                ", orgDomainName='" + orgDomainName + '\'' +
                ", ordererDomainName='" + ordererDomainName + '\'' +
                ", channelName='" + channelName + '\'' +
                ", chaincodeName='" + chaincodeName + '\'' +
                ", chaincodeSource='" + chaincodeSource + '\'' +
                ", chaincodePath='" + chaincodePath + '\'' +
                ", chaincodeVersion='" + chaincodeVersion + '\'' +
                ", proposalWaitTime='" + proposalWaitTime + '\'' +
                ", invokeWaitTime='" + invokeWaitTime + '\'' +
                '}';
    }
}
