package cn.aberic.simple.module.dto;

import cn.aberic.simple.base.BaseDTO;

/**
 * 描述：节点服务器
 *
 * @author : Aberic 【2018/6/5 16:01】
 */
public class PeerDTO extends BaseDTO {

    /** 节点服务器ID */
    private int id;
    /** 节点服务器所属组织ID */
    private int orgId;
    /** 当前指定的组织节点域名 */
    private String peerName;
    /** 当前指定的组织节点事件域名 */
    private String peerEventHubName;
    /** 当前指定的组织节点访问地址 */
    private String peerLocation;
    /** 当前指定的组织节点事件监听访问地址 */
    private String peerEventHubLocation;
    /** 当前peer是否增加Event事件处理 */
    private boolean isEventListener;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
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

    public boolean isEventListener() {
        return isEventListener;
    }

    public void setEventListener(boolean eventListener) {
        isEventListener = eventListener;
    }

    @Override
    public String toString() {
        return "PeerDTO{" +
                "id=" + id +
                ", orgId=" + orgId +
                ", peerName='" + peerName + '\'' +
                ", peerEventHubName='" + peerEventHubName + '\'' +
                ", peerLocation='" + peerLocation + '\'' +
                ", peerEventHubLocation='" + peerEventHubLocation + '\'' +
                ", isEventListener=" + isEventListener +
                '}';
    }
}
