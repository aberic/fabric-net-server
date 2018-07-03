package cn.aberic.fabric.service.impl;

import cn.aberic.fabric.dao.User;
import cn.aberic.fabric.dao.mapper.UserMapper;
import cn.aberic.fabric.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public int init(User user) {
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return 0;
        }
        List<User> users = listAll();
        for (User user1 : users) {
            if (StringUtils.equals(user.getUsername(), user1.getUsername())) {
                return update(user);
            }
        }
        return add(user);
    }

    @Override
    public int add(User user) {
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return 0;
        }
        return userMapper.add(user);
    }

    @Override
    public int update(User user) {
        return userMapper.update(user);
    }

    @Override
    public List<User> listAll() {
        return userMapper.listAll();
    }

    @Override
    public User get(String username) {
        return userMapper.get(username);
    }

}
