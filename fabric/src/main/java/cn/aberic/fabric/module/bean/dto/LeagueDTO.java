package cn.aberic.fabric.module.bean.dto;

import cn.aberic.fabric.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 作者：Aberic on 2018/6/14 00:07
 * 邮箱：abericyang@gmail.com
 */
@ApiModel(value = "联盟对象", description = "联盟对象信息")
public class LeagueDTO extends BaseDTO {

    /** 当前联盟唯一id */
    @ApiModelProperty(value = "当前联盟唯一id")
    private int id;
    /** 当前联盟名称 */
    @ApiModelProperty(value = "当前联盟名称", required = true)
    private String name;
    /** 当前联盟创建日期 */
    @ApiModelProperty(value = "当前联盟创建日期")
    private String date;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "LeagueDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
