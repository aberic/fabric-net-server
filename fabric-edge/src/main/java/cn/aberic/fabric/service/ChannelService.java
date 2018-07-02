package cn.aberic.fabric.service;

import cn.aberic.fabric.dao.Channel;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/27 22:12
 * 邮箱：abericyang@gmail.com
 */
public interface ChannelService {

     int add(Channel channel);

     int update(Channel channel);

     List<Channel> listAll();

     List<Channel> listById(int id);

    Channel get(int id);

     int countById(int id);

     int count();
}
