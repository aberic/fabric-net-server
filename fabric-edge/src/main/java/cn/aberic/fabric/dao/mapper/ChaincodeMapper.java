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

import cn.aberic.fabric.dao.entity.Chaincode;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface ChaincodeMapper {

    @Insert("insert into fns_chaincode (name,path,version,proposal_wait_time,channel_id,cc,date,source,policy," +
            "chaincode_event_listener,callback_location,events) values " +
            "(#{c.name},#{c.path},#{c.version},#{c.proposalWaitTime},#{c.channelId},#{c.cc},#{c.date},#{c.source},#{c.policy}," +
            "#{c.chaincodeEventListener},#{c.callbackLocation},#{c.events})")
    int add(@Param("c") Chaincode chaincode);

    @Update("update fns_chaincode set name=#{c.name}, path=#{c.path}, version=#{c.version}, " +
            "proposal_wait_time=#{c.proposalWaitTime}, chaincode_event_listener=#{c.chaincodeEventListener}, " +
            "callback_location=#{c.callbackLocation}, events=#{c.events} where id=#{c.id}")
    int update(@Param("c") Chaincode chaincode);

    @Update("update fns_chaincode set name=#{c.name}, path=#{c.path}, version=#{c.version}, " +
            "proposal_wait_time=#{c.proposalWaitTime}, source=#{c.source}, policy=#{c.policy}, " +
            "chaincode_event_listener=#{c.chaincodeEventListener}, callback_location=#{c.callbackLocation}, " +
            "events=#{c.events} where id=#{c.id}")
    int updateForUpgrade(@Param("c") Chaincode chaincode);

    @Select("select count(name) from fns_chaincode where channel_id=#{id}")
    int count(@Param("id") int id);

    @Select("select count(name) from fns_chaincode")
    int countAll();

    @Delete("delete from fns_chaincode where id=#{id}")
    int delete(@Param("id") int id);

    @Delete("delete from fns_chaincode where channel_id=#{channelId}")
    int deleteAll(@Param("channelId") int channelId);

    @Select("select id,name,path,version,proposal_wait_time,channel_id,cc,date,source,policy," +
            "chaincode_event_listener,callback_location,events from fns_chaincode " +
            "where name=#{c.name} and path=#{c.path} and version=#{c.version} and channel_id=#{c.channelId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "path", column = "path"),
            @Result(property = "version", column = "version"),
            @Result(property = "proposalWaitTime", column = "proposal_wait_time"),
            @Result(property = "channelId", column = "channel_id"),
            @Result(property = "cc", column = "cc"),
            @Result(property = "chaincodeEventListener", column = "chaincode_event_listener"),
            @Result(property = "callbackLocation", column = "callback_location"),
            @Result(property = "events", column = "events"),
            @Result(property = "date", column = "date"),
            @Result(property = "source", column = "source"),
            @Result(property = "policy", column = "policy")
    })
    Chaincode check(@Param("c") Chaincode chaincode);

    @Select("select id,name,path,version,proposal_wait_time,channel_id,cc,date,source,policy," +
            "chaincode_event_listener,callback_location,events from fns_chaincode where id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "path", column = "path"),
            @Result(property = "version", column = "version"),
            @Result(property = "proposalWaitTime", column = "proposal_wait_time"),
            @Result(property = "channelId", column = "channel_id"),
            @Result(property = "cc", column = "cc"),
            @Result(property = "chaincodeEventListener", column = "chaincode_event_listener"),
            @Result(property = "callbackLocation", column = "callback_location"),
            @Result(property = "events", column = "events"),
            @Result(property = "date", column = "date"),
            @Result(property = "source", column = "source"),
            @Result(property = "policy", column = "policy")
    })
    Chaincode get(@Param("id") int id);

    @Select("select id,name,path,version,proposal_wait_time,channel_id,cc,date,source,policy," +
            "chaincode_event_listener,callback_location,events from fns_chaincode where cc=#{cc}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "path", column = "path"),
            @Result(property = "version", column = "version"),
            @Result(property = "proposalWaitTime", column = "proposal_wait_time"),
            @Result(property = "channelId", column = "channel_id"),
            @Result(property = "cc", column = "cc"),
            @Result(property = "chaincodeEventListener", column = "chaincode_event_listener"),
            @Result(property = "callbackLocation", column = "callback_location"),
            @Result(property = "events", column = "events"),
            @Result(property = "date", column = "date"),
            @Result(property = "source", column = "source"),
            @Result(property = "policy", column = "policy")
    })
    Chaincode getByCC(@Param("cc") String cc);

    @Select("select id,name,path,version,proposal_wait_time,channel_id,cc,date,source,policy," +
            "chaincode_event_listener,callback_location,events from fns_chaincode where channel_id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "path", column = "path"),
            @Result(property = "version", column = "version"),
            @Result(property = "proposalWaitTime", column = "proposal_wait_time"),
            @Result(property = "channelId", column = "channel_id"),
            @Result(property = "cc", column = "cc"),
            @Result(property = "chaincodeEventListener", column = "chaincode_event_listener"),
            @Result(property = "callbackLocation", column = "callback_location"),
            @Result(property = "events", column = "events"),
            @Result(property = "date", column = "date"),
            @Result(property = "source", column = "source"),
            @Result(property = "policy", column = "policy")
    })
    List<Chaincode> list(@Param("id") int id);

    @Select("select id,name,path,version,proposal_wait_time,channel_id,cc,date,source,policy," +
            "chaincode_event_listener,callback_location,events from fns_chaincode")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "path", column = "path"),
            @Result(property = "version", column = "version"),
            @Result(property = "proposalWaitTime", column = "proposal_wait_time"),
            @Result(property = "channelId", column = "channel_id"),
            @Result(property = "cc", column = "cc"),
            @Result(property = "chaincodeEventListener", column = "chaincode_event_listener"),
            @Result(property = "callbackLocation", column = "callback_location"),
            @Result(property = "events", column = "events"),
            @Result(property = "date", column = "date"),
            @Result(property = "source", column = "source"),
            @Result(property = "policy", column = "policy")
    })
    List<Chaincode> listAll();

}
