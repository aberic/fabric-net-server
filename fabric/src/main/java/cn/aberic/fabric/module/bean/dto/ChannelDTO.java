package cn.aberic.fabric.module.bean.dto;

import cn.aberic.fabric.base.BaseDTO;
import cn.aberic.fabric.utils.MD5Helper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 作者：Aberic on 2018/6/14 00:07
 * 邮箱：abericyang@gmail.com
 */
@ApiModel(value = "通道对象", description = "通道对象信息")
public class ChannelDTO  extends BaseDTO {

    /** 通道唯一id */
    @ApiModelProperty(value = "通道唯一id")
    private int id;
    /** 当前组织所访问的通道名称 */
    @ApiModelProperty(value = "当前组织所访问的通道名称", required = true)
    private String name;
    /** 节点id */
    @ApiModelProperty(value = "节点id", required = true)
    private int peerId;

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

    public int getPeerId() {
        return peerId;
    }

    public void setPeerId(int peerId) {
        this.peerId = peerId;
    }

    @Override
    public String toString() {
        return "ChannelDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", peerId=" + peerId +
                '}';
    }
}
