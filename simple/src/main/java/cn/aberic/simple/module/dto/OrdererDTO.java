package cn.aberic.simple.module.dto;

import cn.aberic.simple.base.BaseDTO;
import cn.aberic.simple.utils.MD5Helper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述：排序服务器
 *
 * @author : Aberic 【2018/6/5 15:59】
 */
@ApiModel(value = "排序服务对象", description = "排序服务对象信息")
public class OrdererDTO extends BaseDTO {

    /** 排序服务hash */
    @ApiModelProperty(value = "排序服务hash", required = true)
    private String hash;
    /** 组织hash */
    @ApiModelProperty(value = "组织hash", required = true)
    private String orgHash;
    /** 排序服务器名称 */
    @ApiModelProperty(value = "排序服务器名称", required = true)
    private String name;
    /** 排序服务器地址 */
    @ApiModelProperty(value = "排序服务器地址", required = true)
    private String location;

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

    public String getOrdererHash(){
        return MD5Helper.obtain().md532(hash + name + location);
    }

    @Override
    public String toString() {
        return "OrdererDTO{" +
                "hash='" + hash + '\'' +
                ", orgHash='" + orgHash + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

}
