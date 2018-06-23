package cn.aberic.fabric.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 作者：Aberic on 2018/6/23 10:25
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class Transaction {

    /**块高度*/
    private int num;
    private int txCount;
    private String channelName;
    private String createMSPID;
    private String date;

}
