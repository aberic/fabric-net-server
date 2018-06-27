package cn.aberic.fabric.dao;

import lombok.Getter;
import lombok.Setter;

/**
 * 作者：Aberic on 2018/6/27 21:15
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class Orderer {

    private int id; // required
    private String name; // required
    private String location; // required
    private int orgId; // required
    private String orgName; // required
    private String date; // required

}
