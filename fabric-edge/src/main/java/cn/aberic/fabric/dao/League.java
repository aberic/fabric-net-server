package cn.aberic.fabric.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 作者：Aberic on 2018/6/27 21:12
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
@ToString
public class League {

    private int id; // required
    private String name; // required
    private String date; // required
    private int orgCount; // required

}
