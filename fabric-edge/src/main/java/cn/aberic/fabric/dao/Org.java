package cn.aberic.fabric.dao;

import lombok.Getter;
import lombok.Setter;

/**
 * 作者：Aberic on 2018/6/27 21:15
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class Org {

    private int id; // required
    private String name; // required
    private boolean tls; // required
    private String username; // required
    private String cryptoConfigDir; // required
    private String mspId; // required
    private String domainName; // required
    private String ordererDomainName; // required
    private int leagueId; // required
    private String leagueName; // required
    private String date; // required
    private int peerCount; // required
    private int ordererCount; // required
}
