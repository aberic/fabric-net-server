package cn.aberic.fabric.service;

import cn.aberic.fabric.dao.Peer;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/27 22:03
 * 邮箱：abericyang@gmail.com
 */
public interface PeerService {
    
    int add(Peer peer);

    int update(Peer peer);

    List<Peer> listAll();

    List<Peer> listById(int id);

    Peer get(int id);

    int countById(int id);

    int count();

    int delete(int id);
}
