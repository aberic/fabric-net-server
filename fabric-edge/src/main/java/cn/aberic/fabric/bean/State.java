package cn.aberic.fabric.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/27 21:16
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class State {

    private int id; // required
    private List<String> strArray; // required
}
