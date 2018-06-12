package cn.aberic.simple.module.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述：溯源对象
 *
 * @author : Aberic 【2018/6/12 13:48】
 */
@ApiModel(value = "溯源对象", description = "溯源操作所需转接对象")
public class TraceDTO {

    /** 当前执行合约的组织hash */
    @ApiModelProperty(value = "当前执行合约的组织hash", required = true)
    private String hash;
    /** 当前组织链查询id */
    @ApiModelProperty(value = "当前组织链查询id", required = true)
    private String trace;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }
}
