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

import cn.aberic.fabric.dao.entity.Peer;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface PeerMapper {

    @Insert("insert into fns_peer (name,location,event_hub_location,server_crt_path,client_cert_path,client_key_path,org_id,date) values" +
            " (#{p.name},#{p.location},#{p.eventHubLocation},#{p.serverCrtPath},#{p.clientCertPath},#{p.clientKeyPath},#{p.orgId},#{p.date})")
    int add(@Param("p") Peer peer);

    @Update("update fns_peer set name=#{p.name}, location=#{p.location}" +
            ", event_hub_location=#{p.eventHubLocation}" +
            ", server_crt_path=#{p.serverCrtPath}, " +
            "client_cert_path=#{p.clientCertPath}, " +
            "client_key_path=#{p.clientKeyPath} where id=#{p.id}")
    int update(@Param("p") Peer peer);

    @Update("update fns_peer set name=#{p.name}, location=#{p.location}" +
            ", event_hub_location=#{p.eventHubLocation} where id=#{p.id}")
    int updateWithNoFile(@Param("p") Peer peer);

    @Select("select count(name) from fns_peer where org_id=#{id}")
    int count(@Param("id") int id);

    @Select("select count(name) from fns_peer")
    int countAll();

    @Delete("delete from fns_peer where id=#{id}")
    int delete(@Param("id") int id);

    @Delete("delete from fns_peer where org_id=#{orgId}")
    int deleteAll(@Param("orgId") int orgId);

    @Select("select id,name,location,event_hub_location,server_crt_path,client_cert_path,client_key_path,org_id,date from fns_peer where id=#{id}")
    @Results({
            @Result(property = "eventHubLocation", column = "event_hub_location"),
            @Result(property = "serverCrtPath", column = "server_crt_path"),
            @Result(property = "clientCertPath", column = "client_cert_path"),
            @Result(property = "clientKeyPath", column = "client_key_path"),
            @Result(property = "orgId", column = "org_id")
    })
    Peer get(@Param("id") int id);

    @Select("select id,name,location,event_hub_location,server_crt_path,client_cert_path,client_key_path,org_id,date from fns_peer where org_id=#{id}")
    @Results({
            @Result(property = "eventHubLocation", column = "event_hub_location"),
            @Result(property = "serverCrtPath", column = "server_crt_path"),
            @Result(property = "clientCertPath", column = "client_cert_path"),
            @Result(property = "clientKeyPath", column = "client_key_path"),
            @Result(property = "orgId", column = "org_id")
    })
    List<Peer> list(@Param("id") int id);

    @Select("select id,name,location,event_hub_location,server_crt_path,client_cert_path,client_key_path,org_id,date from fns_peer")
    @Results({
            @Result(property = "eventHubLocation", column = "event_hub_location"),
            @Result(property = "serverCrtPath", column = "server_crt_path"),
            @Result(property = "clientCertPath", column = "client_cert_path"),
            @Result(property = "clientKeyPath", column = "client_key_path"),
            @Result(property = "orgId", column = "org_id")
    })
    List<Peer> listAll();

}
