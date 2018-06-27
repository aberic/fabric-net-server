package cn.aberic.fabric.dao;

import lombok.Getter;
import lombok.Setter;

/**
 * 作者：Aberic on 2018/6/27 21:15
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class Peer {

    private int id; // required
    private String name; // required
    private String eventHubName; // required
    private String location; // required
    private String eventHubLocation; // required
    private boolean eventListener; // required
    private int orgId; // required
    private String orgName; // required
    private String date; // required
    private int channelCount; // required
}
