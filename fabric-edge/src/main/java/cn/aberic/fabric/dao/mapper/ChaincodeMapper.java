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

import cn.aberic.fabric.dao.Chaincode;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface ChaincodeMapper {

    @Insert("insert into chaincode (name,path,version,proposal_wait_time,invoke_wait_time,channel_id,date,source,policy) values " +
            "(#{c.name},#{c.path},#{c.version},#{c.proposalWaitTime},#{c.invokeWaitTime},#{c.channelId},#{c.date},#{c.source},#{c.policy})")
    int add(@Param("c") Chaincode chaincode);

    @Update("update chaincode set name=#{c.name}, path=#{c.path}, version=#{c.version}, " +
            "proposal_wait_time=#{c.proposalWaitTime}, invoke_wait_time=#{c.invokeWaitTime} where rowid=#{c.id}")
    int update(@Param("c") Chaincode chaincode);

    @Update("update chaincode set name=#{c.name}, path=#{c.path}, version=#{c.version}, " +
            "proposal_wait_time=#{c.proposalWaitTime}, invoke_wait_time=#{c.invokeWaitTime}, " +
            "source=#{c.source}, policy=#{c.policy} where rowid=#{c.id}")
    int updateForUpgrade(@Param("c") Chaincode chaincode);

    @Select("select count(name) from chaincode where channel_id=#{id}")
    int count(@Param("id") int id);

    @Select("select count(name) from chaincode")
    int countAll();

    @Delete("delete from chaincode where rowid=#{id}")
    int delete(@Param("id") int id);

    @Delete("delete from chaincode where channel_id=#{channelId}")
    int deleteAll(@Param("channelId") int channelId);

    @Select("select rowid,name,path,version,proposal_wait_time,invoke_wait_time,channel_id,date,source,policy from chaincode " +
            "where name=#{c.name} and path=#{c.path} and version=#{c.version} and channel_id=#{c.channelId}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "path", column = "path"),
            @Result(property = "chaincode_version", column = "chaincode_version"),
            @Result(property = "proposalWaitTime", column = "chaincode_proposal_wait_time"),
            @Result(property = "invokeWaitTime", column = "chaincode_invoke_wait_time"),
            @Result(property = "channelId", column = "channel_id"),
            @Result(property = "date", column = "date"),
            @Result(property = "source", column = "source"),
            @Result(property = "policy", column = "policy")
    })
    Chaincode check(@Param("c") Chaincode chaincode);

    @Select("select rowid,name,path,version,proposal_wait_time,invoke_wait_time,channel_id,date,source,policy from chaincode where rowid=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "path", column = "path"),
            @Result(property = "chaincode_version", column = "chaincode_version"),
            @Result(property = "proposalWaitTime", column = "chaincode_proposal_wait_time"),
            @Result(property = "invokeWaitTime", column = "chaincode_invoke_wait_time"),
            @Result(property = "channelId", column = "channel_id"),
            @Result(property = "date", column = "date"),
            @Result(property = "source", column = "source"),
            @Result(property = "policy", column = "policy")
    })
    Chaincode get(@Param("id") int id);

    @Select("select rowid,name,path,version,proposal_wait_time,invoke_wait_time,channel_id,date,source,policy from chaincode where channel_id=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "path", column = "path"),
            @Result(property = "chaincode_version", column = "chaincode_version"),
            @Result(property = "proposalWaitTime", column = "chaincode_proposal_wait_time"),
            @Result(property = "invokeWaitTime", column = "chaincode_invoke_wait_time"),
            @Result(property = "channelId", column = "channel_id"),
            @Result(property = "date", column = "date"),
            @Result(property = "source", column = "source"),
            @Result(property = "policy", column = "policy")
    })
    List<Chaincode> list(@Param("id") int id);

    @Select("select rowid,name,path,version,proposal_wait_time,invoke_wait_time,channel_id,date,source,policy from chaincode")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "path", column = "path"),
            @Result(property = "chaincode_version", column = "chaincode_version"),
            @Result(property = "proposalWaitTime", column = "chaincode_proposal_wait_time"),
            @Result(property = "invokeWaitTime", column = "chaincode_invoke_wait_time"),
            @Result(property = "channelId", column = "channel_id"),
            @Result(property = "date", column = "date"),
            @Result(property = "source", column = "source"),
            @Result(property = "policy", column = "policy")
    })
    List<Chaincode> listAll();

}
