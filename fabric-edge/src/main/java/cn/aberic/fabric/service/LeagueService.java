package cn.aberic.fabric.service;

import cn.aberic.fabric.dao.League;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/27 22:11
 * 邮箱：abericyang@gmail.com
 */
public interface LeagueService {

    int add(League league);

    int update(League league);

    List<League> listAll();

    League get(int id);

    int delete(int id);
}
