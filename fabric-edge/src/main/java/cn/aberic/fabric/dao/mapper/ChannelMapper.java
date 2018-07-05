/*
 * Copyright (c) 2018. Aberic - aberic@qq.com - All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.aberic.fabric.dao.mapper;

import cn.aberic.fabric.dao.Channel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface ChannelMapper {

    @Insert("insert into channel (name,peer_id,date) values (#{c.name},#{c.peerId},#{c.date})")
    int add(@Param("c") Channel channel);

    @Update("update channel set name=#{c.name} where rowid=#{c.id}")
    int update(@Param("c") Channel channel);

    @Select("select count(name) from channel where peer_id=#{id}")
    int count(@Param("id") int id);

    @Select("select count(name) from channel")
    int countAll();

    @Delete("delete from channel where rowid=#{id}")
    int delete(@Param("id") int id);

    @Delete("delete from channel where peer_id=#{peerId}")
    int deleteAll(@Param("peerId") int peerId);

    @Select("select rowid,name,peer_id,date from channel where name=#{c.name} and peer_id=#{c.peerId}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "peerId", column = "peer_id"),
            @Result(property = "date", column = "date")
    })
    Channel check(@Param("c") Channel channel);

    @Select("select rowid,name,peer_id,date from channel where rowid=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "peerId", column = "peer_id"),
            @Result(property = "date", column = "date")
    })
    Channel get(@Param("id") int id);

    @Select("select rowid,name,peer_id,date from channel where peer_id=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "peerId", column = "peer_id"),
            @Result(property = "date", column = "date")
    })
    List<Channel> list(@Param("id") int id);

    @Select("select rowid,name,peer_id,date from channel")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "peerId", column = "peer_id"),
            @Result(property = "date", column = "date")
    })
    List<Channel> listAll();

}
