package cn.aberic.simple.module.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 描述：合约对象
 *
 * @author : Aberic 【2018/6/12 11:36】
 */
@ApiModel(value = "合约对象", description = "操作合约所需转接对象")
public class ChainCodeDTO {

    /** 当前执行合约的组织hash */
    @ApiModelProperty(value = "当前执行合约的组织hash", required = true)
    private String hash;
    /** 当前执行合约的参数集合 */
    @ApiModelProperty(value = "当前执行合约的参数集合", required = true)
    private List<String> args;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }
}
