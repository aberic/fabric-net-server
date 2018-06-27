package cn.aberic.fabric.dao;

import lombok.Getter;
import lombok.Setter;

/**
 * 作者：Aberic on 2018/6/27 21:15
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class Channel {

    private int id; // required
    private String name; // required
    private int peerId; // required
    private String date; // optional
    private String peerName; // optional
    private String orgName; // optional
    private String leagueName; // optional
    private int chaincodeCount; // optional
}
