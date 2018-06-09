package cn.aberic.simple.module.dto;

import cn.aberic.simple.base.BaseDTO;

/**
 * 描述：排序服务器
 *
 * @author : Aberic 【2018/6/5 15:59】
 */
public class OrdererDTO extends BaseDTO {

    /** 组织hash */
    private String hash;
    /** 排序服务器名称 */
    private String name;
    /** 排序服务器地址 */
    private String location;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
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
                "hash=" + hash +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
