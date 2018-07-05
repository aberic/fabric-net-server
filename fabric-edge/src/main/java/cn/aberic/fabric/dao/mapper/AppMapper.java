/*
 * Copyright (c) 2018. Aberic - All Rights Reserved.
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

import cn.aberic.fabric.bean.App;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface AppMapper {

    @Insert("insert into app (name, key, chaincode_id, create_date, modify_date, private_key, public_key, active)" +
            " values (#{a.name},#{a.key},#{a.chaincodeId},#{a.createDate},#{a.modifyDate},#{a.privateKey},#{a.publicKey},#{a.active})")
    int add(@Param("a") App app);

    @Update("update app set name=#{a.name}, key=#{a.key}, modify_date=#{a.modifyDate}, active=#{a.active} where rowid=#{a.id}")
    int update(@Param("a") App app);

    @Update("update app set private_key=#{a.privateKey}, public_key=#{a.publicKey} where rowid=#{a.id}")
    int updateKey(@Param("a") App app);

    @Select("select count(name) from app where chaincode_id=#{id}")
    int count(@Param("id") int id);

    @Select("select name, key, chaincode_id, create_date, modify_date, public_key, active from app where chaincode_id=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "key", column = "key"),
            @Result(property = "chaincode_id", column = "chaincodeId"),
            @Result(property = "create_date", column = "createDate"),
            @Result(property = "modify_date", column = "modifyDate"),
            @Result(property = "public_key", column = "publicKey"),
            @Result(property = "active", column = "active")
    })
    List<App> list(@Param("id") int id);

    @Select("select name, key, chaincode_id, create_date, modify_date, public_key, active from app where rowid=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "key", column = "key"),
            @Result(property = "chaincode_id", column = "chaincodeId"),
            @Result(property = "create_date", column = "createDate"),
            @Result(property = "modify_date", column = "modifyDate"),
            @Result(property = "public_key", column = "publicKey"),
            @Result(property = "active", column = "active")
    })
    App get(@Param("id") int id);

    @Delete("delete from app where rowid=#{id}")
    int delete(@Param("id") int id);

}
