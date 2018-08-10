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

import cn.aberic.fabric.dao.Peer;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface PeerMapper {

    @Insert("insert into peer (name,location,event_hub_location,server_crt_path,org_id,date) " +
            "values (#{p.name},#{p.location},#{p.eventHubLocation},#{p.serverCrtPath},#{p.orgId},#{p.date})")
    int add(@Param("p") Peer peer);

    @Update("update peer set name=#{p.name}, location=#{p.location}" +
            ", event_hub_location=#{p.eventHubLocation}" +
            ", server_crt_path=#{p.serverCrtPath} where id=#{p.id}")
    int update(@Param("p") Peer peer);

    @Update("update peer set name=#{p.name}, location=#{p.location}" +
            ", event_hub_location=#{p.eventHubLocation} where id=#{p.id}")
    int updateWithNoFile(@Param("p") Peer peer);

    @Select("select count(name) from peer where org_id=#{id}")
    int count(@Param("id") int id);

    @Select("select count(name) from peer")
    int countAll();

    @Delete("delete from peer where id=#{id}")
    int delete(@Param("id") int id);

    @Delete("delete from peer where org_id=#{orgId}")
    int deleteAll(@Param("orgId") int orgId);

    @Select("select id,name,location,event_hub_location,server_crt_path,org_id,date from peer where id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "location", column = "location"),
            @Result(property = "eventHubLocation", column = "event_hub_location"),
            @Result(property = "serverCrtPath", column = "server_crt_path"),
            @Result(property = "orgId", column = "org_id"),
            @Result(property = "date", column = "date")
    })
    Peer get(@Param("id") int id);

    @Select("select id,name,location,event_hub_location,server_crt_path,org_id,date from peer where org_id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "location", column = "location"),
            @Result(property = "eventHubLocation", column = "event_hub_location"),
            @Result(property = "serverCrtPath", column = "server_crt_path"),
            @Result(property = "orgId", column = "org_id"),
            @Result(property = "date", column = "date")
    })
    List<Peer> list(@Param("id") int id);

    @Select("select id,name,location,event_hub_location,server_crt_path,org_id,date from peer")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "location", column = "location"),
            @Result(property = "eventHubLocation", column = "event_hub_location"),
            @Result(property = "serverCrtPath", column = "server_crt_path"),
            @Result(property = "orgId", column = "org_id"),
            @Result(property = "date", column = "date")
    })
    List<Peer> listAll();

}
