package cn.aberic.fabric.service;

import cn.aberic.fabric.bean.State;

/**
 * 作者：Aberic on 2018/6/27 22:02
 * 邮箱：abericyang@gmail.com
 */
public interface StateService {

    String invoke(State state);

    String query(State state);

}
