package cn.aberic.fabric.service;

import cn.aberic.fabric.dao.Orderer;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/27 22:09
 * 邮箱：abericyang@gmail.com
 */
public interface OrdererService {

     int add(Orderer orderer);

     int update(Orderer orderer);

     List<Orderer> listAll();

     List<Orderer> listById(int id);

     Orderer get(int id);

     int countById(int id);

     int count();
}
