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

import cn.aberic.fabric.dao.entity.Channel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface ChannelMapper {

    @Insert("insert into channel (name,block_listener,callback_location,peer_id,date) values " +
            "(#{c.name},#{c.blockListener},#{c.callbackLocation},#{c.peerId},#{c.date})")
    int add(@Param("c") Channel channel);

    @Update("update channel set name=#{c.name}, block_listener=#{c.blockListener}, " +
            "callback_location=#{c.callbackLocation} where id=#{c.id}")
    int update(@Param("c") Channel channel);

    @Update("update channel set height=#{height} where id=#{id}")
    int updateHeight(@Param("id") int id, @Param("height") int height);

    @Select("select count(name) from channel where peer_id=#{id}")
    int count(@Param("id") int id);

    @Select("select count(name) from channel")
    int countAll();

    @Delete("delete from channel where id=#{id}")
    int delete(@Param("id") int id);

    @Delete("delete from channel where peer_id=#{peerId}")
    int deleteAll(@Param("peerId") int peerId);

    @Select("select id,name,block_listener,callback_location,peer_id,date from channel where name=#{c.name} and peer_id=#{c.peerId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "blockListener", column = "block_listener"),
            @Result(property = "callbackLocation", column = "callback_location"),
            @Result(property = "peerId", column = "peer_id"),
            @Result(property = "date", column = "date")
    })
    Channel check(@Param("c") Channel channel);

    @Select("select id,name,block_listener,callback_location,peer_id,date from channel where id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "blockListener", column = "block_listener"),
            @Result(property = "callbackLocation", column = "callback_location"),
            @Result(property = "peerId", column = "peer_id"),
            @Result(property = "date", column = "date")
    })
    Channel get(@Param("id") int id);

    @Select("select id,name,block_listener,callback_location,peer_id,date from channel where peer_id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "blockListener", column = "block_listener"),
            @Result(property = "callbackLocation", column = "callback_location"),
            @Result(property = "peerId", column = "peer_id"),
            @Result(property = "date", column = "date")
    })
    List<Channel> list(@Param("id") int id);

    @Select("select id,name,block_listener,callback_location,height,peer_id,date from channel")
    @Results({
            @Result(property = "blockListener", column = "block_listener"),
            @Result(property = "callbackLocation", column = "callback_location"),
            @Result(property = "peerId", column = "peer_id")
    })
    List<Channel> listAll();

}
