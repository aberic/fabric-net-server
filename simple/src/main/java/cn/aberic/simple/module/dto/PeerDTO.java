package cn.aberic.simple.module.dto;

import cn.aberic.simple.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述：节点服务器
 *
 * @author : Aberic 【2018/6/5 16:01】
 */
@ApiModel(value = "节点服务对象", description = "节点服务对象信息")
public class PeerDTO extends BaseDTO {

    /** 节点hash */
    @ApiModelProperty(value = "节点hash", required = true)
    private String hash;
    /** 组织hash */
    @ApiModelProperty(value = "组织hash", required = true)
    private String orgHash;
    /** 当前指定的组织节点域名 */
    @ApiModelProperty(value = "当前指定的组织节点域名", required = true)
    private String peerName;
    /** 当前指定的组织节点事件域名 */
    @ApiModelProperty(value = "当前指定的组织节点事件域名", required = true)
    private String peerEventHubName;
    /** 当前指定的组织节点访问地址 */
    @ApiModelProperty(value = "当前指定的组织节点访问地址", required = true)
    private String peerLocation;
    /** 当前指定的组织节点事件监听访问地址 */
    @ApiModelProperty(value = "当前指定的组织节点事件监听访问地址", required = true)
    private String peerEventHubLocation;
    /** 当前peer是否增加Event事件处理，0=false，1=true */
    @ApiModelProperty(value = "当前peer是否增加Event事件处理，0=false，1=true", required = true)
    private int isEventListener;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getOrgHash() {
        return orgHash;
    }

    public void setOrgHash(String orgHash) {
        this.orgHash = orgHash;
    }

    public String getPeerName() {
        return peerName;
    }

    public void setPeerName(String peerName) {
        this.peerName = peerName;
    }

    public String getPeerEventHubName() {
        return peerEventHubName;
    }

    public void setPeerEventHubName(String peerEventHubName) {
        this.peerEventHubName = peerEventHubName;
    }

    public String getPeerLocation() {
        return peerLocation;
    }

    public void setPeerLocation(String peerLocation) {
        this.peerLocation = peerLocation;
    }

    public String getPeerEventHubLocation() {
        return peerEventHubLocation;
    }

    public void setPeerEventHubLocation(String peerEventHubLocation) {
        this.peerEventHubLocation = peerEventHubLocation;
    }

    public int isEventListener() {
        return isEventListener;
    }

    public void setEventListener(int eventListener) {
        isEventListener = eventListener;
    }

    @Override
    public String toString() {
        return "PeerDTO{" +
                "hash='" + hash + '\'' +
                ", orgHash='" + orgHash + '\'' +
                ", peerName='" + peerName + '\'' +
                ", peerEventHubName='" + peerEventHubName + '\'' +
                ", peerLocation='" + peerLocation + '\'' +
                ", peerEventHubLocation='" + peerEventHubLocation + '\'' +
                ", isEventListener=" + isEventListener +
                '}';
    }
}
