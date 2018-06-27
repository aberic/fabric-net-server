package cn.aberic.fabric.dao;

import lombok.Getter;
import lombok.Setter;

/**
 * 作者：Aberic on 2018/6/27 21:16
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class Chaincode {

    private int id; // required
    private String name; // required
    private String source; // optional
    private String path; // optional
    private String policy; // optional
    private String version; // required
    private int proposalWaitTime; // required
    private int invokeWaitTime; // required
    private int channelId; // required
    private String date; // optional
    private String channelName; // optional
    private String peerName; // optional
    private String orgName; // optional
    private String leagueName; // optional
}
