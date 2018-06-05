package cn.aberic.simple.module.dto;

import cn.aberic.simple.base.BaseDTO;

/**
 * 描述：排序服务器
 *
 * @author : Aberic 【2018/6/5 15:59】
 */
public class OrdererDTO extends BaseDTO {

    /** 排序服务器ID */
    private int id;
    /** 排序服务器所属组织ID */
    private int orgId;
    /** 排序服务器名称 */
    private String name;
    /** 排序服务器地址 */
    private String location;

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

    @Override
    public String toString() {
        return "OrdererDTO{" +
                "id=" + id +
                ", orgId=" + orgId +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
