package cn.aberic.fabric.module.bean.vo;

import cn.aberic.fabric.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 描述：状态对象
 *
 * @author : Aberic 【2018/6/12 11:36】
 */
@ApiModel(value = "状态对象", description = "操作合约所需状态对象")
public class StateVO extends BaseVO {

    /** 当前正在运行的智能合约id */
    @ApiModelProperty(value = "当前正在运行的智能合约id", required = true)
    private int id;
    /** 当前执行合约的参数集合 */
    @ApiModelProperty(value = "当前执行合约的参数集合", required = true)
    private List<String> args;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }
}
