package cn.aberic.fabric.module.bean.vo;

import cn.aberic.fabric.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 作者：Aberic on 2018/6/14 00:07
 * 邮箱：abericyang@gmail.com
 */
@ApiModel(value = "联盟对象", description = "联盟对象信息")
public class LeagueVO extends BaseDTO {


    /** 当前联盟唯一id */
    @ApiModelProperty(value = "当前联盟唯一id")
    private int id;
    /** 当前联盟名称 */
    @ApiModelProperty(value = "当前联盟名称", required = true)
    private String name;
    /** 当前联盟创建日期 */
    @ApiModelProperty(value = "当前联盟创建日期")
    private String date;
    /** 当前联盟下组织数量 */
    @ApiModelProperty(value = "当前联盟下组织数量")
    private int count;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "LeagueVO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", count=" + count +
                '}';
    }
}
