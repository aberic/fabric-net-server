package cn.aberic.thrift.vo;

import cn.aberic.thrift.vo.base.BaseVO;
import lombok.Getter;
import lombok.Setter;

/**
 * 作者：Aberic on 2018/6/21 23:46
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class ChannelVO extends BaseVO {

    /** 通道唯一id */
    private int id;
    /** 当前组织所访问的通道名称 */
    private String name;
    /** 节点id */
    private int peerId;
    /** 节点名称 */
    private String peerName;
    /** 组织名称 */
    private String orgName;
    /** 联盟名称 */
    private String leagueName;
    /** 当前通道下合约数量 */
    private int count;

}
