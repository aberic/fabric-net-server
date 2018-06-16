package cn.aberic.fabric.module.bean.dto;

import cn.aberic.fabric.base.BaseDTO;
import cn.aberic.fabric.utils.MD5Helper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述：排序服务器
 *
 * @author : Aberic 【2018/6/5 15:59】
 */
@ApiModel(value = "排序服务对象", description = "排序服务对象信息")
public class OrdererDTO extends BaseDTO {

    /** 组织唯一id */
    @ApiModelProperty(value = "组织唯一id")
    private int id;
    /** 排序服务器名称 */
    @ApiModelProperty(value = "排序服务器名称", required = true)
    private String name;
    /** 排序服务器地址 */
    @ApiModelProperty(value = "排序服务器地址", required = true)
    private String location;
    /** 组织id */
    @ApiModelProperty(value = "组织id", required = true)
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return "OrdererDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", orgId=" + orgId +
                '}';
    }
}

