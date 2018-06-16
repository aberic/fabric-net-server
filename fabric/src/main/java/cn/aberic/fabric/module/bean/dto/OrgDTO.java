package cn.aberic.fabric.module.bean.dto;

import cn.aberic.fabric.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述：使用指定用户访问指定通道指定智能合约的组织信息
 *
 * @author : Aberic 【2018/6/5 15:51】
 */
@ApiModel(value = "组织对象", description = "组织对象信息")
public class OrgDTO extends BaseDTO {

    /** 组织唯一id */
    @ApiModelProperty(value = "组织唯一id")
    private int id;
    /** 组织名称 */
    @ApiModelProperty(value = "组织名称", required = true)
    private String name;
    /** 是否开启TLS，0=false，1=true */
    @ApiModelProperty(value = "是否开启TLS", required = true)
    private boolean tls;
    /** 设置默认用户 */
    @ApiModelProperty(value = "设置默认用户", required = true)
    private String username;
    /** CryptoConfig所在目录的目录名称 */
    @ApiModelProperty(value = "CryptoConfig所在目录的目录名称", required = true)
    private String cryptoConfigDir;
    /** 组织唯一标识符 */
    @ApiModelProperty(value = "组织唯一标识符", required = true)
    private String mspId;
    /** 组织域名 */
    @ApiModelProperty(value = "组织域名", required = true)
    private String domainName;
    /** 排序服务域名 */
    @ApiModelProperty(value = "排序服务域名", required = true)
    private String ordererDomainName;
    /** 联盟id */
    @ApiModelProperty(value = "联盟id", required = true)
    private int leagueId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTls() {
        return tls;
    }

    public void setTls(boolean tls) {
        this.tls = tls;
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

    public String getMspId() {
        return mspId;
    }

    public void setMspId(String mspId) {
        this.mspId = mspId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getOrdererDomainName() {
        return ordererDomainName;
    }

    public void setOrdererDomainName(String ordererDomainName) {
        this.ordererDomainName = ordererDomainName;
    }

    public int getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    @Override
    public String toString() {
        return "OrgDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", tls=" + tls +
                ", username='" + username + '\'' +
                ", cryptoConfigDir='" + cryptoConfigDir + '\'' +
                ", mspId='" + mspId + '\'' +
                ", domainName='" + domainName + '\'' +
                ", ordererDomainName='" + ordererDomainName + '\'' +
                ", leagueId=" + leagueId +
                '}';
    }
}
