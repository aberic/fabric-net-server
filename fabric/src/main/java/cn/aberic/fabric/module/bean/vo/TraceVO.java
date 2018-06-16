package cn.aberic.fabric.module.bean.vo;

import cn.aberic.fabric.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述：溯源对象
 *
 * @author : Aberic 【2018/6/12 13:48】
 */
@ApiModel(value = "溯源对象", description = "溯源操作所需转接对象")
public class TraceVO extends BaseVO {

    /** 当前正在运行的智能合约id */
    @ApiModelProperty(value = "当前正在运行的智能合约id", required = true)
    private int chainCodeId;
    /** 当前组织链查询id */
    @ApiModelProperty(value = "当前组织链查询id")
    private String trace;

    public int getChainCodeId() {
        return chainCodeId;
    }

    public void setChainCodeId(int chainCodeId) {
        this.chainCodeId = chainCodeId;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }
}
