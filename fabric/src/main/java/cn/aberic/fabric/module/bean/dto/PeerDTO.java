package cn.aberic.fabric.module.bean.dto;

import cn.aberic.fabric.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述：节点服务器
 *
 * @author : Aberic 【2018/6/5 16:01】
 */
@ApiModel(value = "节点服务对象", description = "节点服务对象信息")
public class PeerDTO extends BaseDTO {

    /** 节点唯一id */
    @ApiModelProperty(value = "节点唯一id")
    private int id;
    /** 当前指定的组织节点域名 */
    @ApiModelProperty(value = "当前指定的组织节点域名", required = true)
    private String name;
    /** 当前指定的组织节点事件域名 */
    @ApiModelProperty(value = "当前指定的组织节点事件域名", required = true)
    private String eventHubName;
    /** 当前指定的组织节点访问地址 */
    @ApiModelProperty(value = "当前指定的组织节点访问地址", required = true)
    private String location;
    /** 当前指定的组织节点事件监听访问地址 */
    @ApiModelProperty(value = "当前指定的组织节点事件监听访问地址", required = true)
    private String eventHubLocation;
    /** 当前peer是否增加Event事件处理，0=false，1=true */
    @ApiModelProperty(value = "当前peer是否增加Event事件处理", required = true)
    private boolean isEventListener;
    /** 节点id */
    @ApiModelProperty(value = "节点id", required = true)
    private int orgId;

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

    public String getEventHubName() {
        return eventHubName;
    }

    public void setEventHubName(String eventHubName) {
        this.eventHubName = eventHubName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEventHubLocation() {
        return eventHubLocation;
    }

    public void setEventHubLocation(String eventHubLocation) {
        this.eventHubLocation = eventHubLocation;
    }

    public boolean isEventListener() {
        return isEventListener;
    }

    public void setEventListener(boolean eventListener) {
        isEventListener = eventListener;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return "PeerDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", eventHubName='" + eventHubName + '\'' +
                ", location='" + location + '\'' +
                ", eventHubLocation='" + eventHubLocation + '\'' +
                ", isEventListener=" + isEventListener +
                ", orgId=" + orgId +
                '}';
    }
}
