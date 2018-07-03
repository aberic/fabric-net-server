package cn.aberic.fabric.service;

import cn.aberic.fabric.dao.User;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/27 22:11
 * 邮箱：abericyang@gmail.com
 */
public interface UserService {

    int init(User user);

    int add(User user);

    int update(User user);

    List<User> listAll();

    User get(String username);
}
