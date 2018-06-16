package cn.aberic.fabric.module.bean.dto;

import cn.aberic.fabric.base.BaseDTO;
import cn.aberic.fabric.utils.MD5Helper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 作者：Aberic on 2018/6/14 00:07
 * 邮箱：abericyang@gmail.com
 */
@ApiModel(value = "智能合约对象", description = "智能合约对象信息")
public class ChainCodeDTO  extends BaseDTO {

    /** 合约唯一id */
    @ApiModelProperty(value = "合约唯一id")
    private int id;
    /** 智能合约名称 */
    @ApiModelProperty(value = "智能合约名称", required = true)
    private String name;
    /** 智能合约路径 */
    @ApiModelProperty(value = "智能合约路径", required = true)
    private String path;
    /** 智能合约版本 */
    @ApiModelProperty(value = "智能合约版本", required = true)
    private String version;
    /** 单个提案请求的超时时间以毫秒为单位 */
    @ApiModelProperty(value = "单个提案请求的超时时间以毫秒为单位", required = true)
    private int proposalWaitTime;
    /** 事务等待时间以秒为单位 */
    @ApiModelProperty(value = "事务等待时间以秒为单位", required = true)
    private int invokeWaitTime;
    /** 通道id */
    @ApiModelProperty(value = "通道id", required = true)
    private int channelId;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        return "ChainCodeDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", version='" + version + '\'' +
                ", proposalWaitTime=" + proposalWaitTime +
                ", invokeWaitTime=" + invokeWaitTime +
                ", channelId=" + channelId +
                '}';
    }
}
