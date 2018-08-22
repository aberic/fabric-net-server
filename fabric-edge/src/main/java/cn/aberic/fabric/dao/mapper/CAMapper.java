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

import cn.aberic.fabric.dao.entity.CA;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/7/12 21:01
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface CAMapper {

    @Insert("insert into fns_ca (name,sk_path,certificate_path,flag,peer_id,date) values " +
            "(#{c.name},#{c.skPath},#{c.certificatePath},#{c.flag},#{c.peerId},#{c.date})")
    int add(@Param("c") CA ca);

    @Update("update fns_ca set name=#{c.name},sk_path=#{c.skPath},certificate_path=#{c.certificatePath},flag=#{c.flag} where id=#{c.id}")
    int update(@Param("c") CA ca);

    @Update("update fns_ca set name=#{c.name},flag=#{c.flag} where id=#{c.id}")
    int updateWithNoFile(@Param("c") CA ca);

    @Select("select count(name) from fns_ca where peer_id=#{id}")
    int count(@Param("id") int id);

    @Select("select count(name) from fns_ca")
    int countAll();

    @Delete("delete from fns_ca where id=#{id}")
    int delete(@Param("id") int id);

    @Delete("delete from fns_ca where peer_id=#{peerId}")
    int deleteAll(@Param("peerId") int peerId);

    @Select("select id,name,sk_path,certificate_path,flag,peer_id,date from fns_ca where name=#{c.name} and peer_id=#{c.peerId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "skPath", column = "sk_path"),
            @Result(property = "certificatePath", column = "certificate_path"),
            @Result(property = "flag", column = "flag"),
            @Result(property = "peerId", column = "peer_id"),
            @Result(property = "date", column = "date")
    })
    CA check(@Param("c") CA ca);

    @Select("select id,name,sk_path,certificate_path,flag,peer_id,date from fns_ca where id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "skPath", column = "sk_path"),
            @Result(property = "certificatePath", column = "certificate_path"),
            @Result(property = "flag", column = "flag"),
            @Result(property = "peerId", column = "peer_id"),
            @Result(property = "date", column = "date")
    })
    CA get(@Param("id") int id);

    @Select("select id,name,sk_path,certificate_path,flag,peer_id,date from fns_ca where flag=#{flag}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "skPath", column = "sk_path"),
            @Result(property = "certificatePath", column = "certificate_path"),
            @Result(property = "flag", column = "flag"),
            @Result(property = "peerId", column = "peer_id"),
            @Result(property = "date", column = "date")
    })
    CA getByFlag(@Param("flag") String flag);

    @Select("select id,name,sk_path,certificate_path,flag,peer_id,date from fns_ca where peer_id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "skPath", column = "sk_path"),
            @Result(property = "certificatePath", column = "certificate_path"),
            @Result(property = "flag", column = "flag"),
            @Result(property = "peerId", column = "peer_id"),
            @Result(property = "date", column = "date")
    })
    List<CA> list(@Param("id") int id);

    @Select("select id,name,sk_path,certificate_path,flag,peer_id,date from fns_ca")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "skPath", column = "sk_path"),
            @Result(property = "certificatePath", column = "certificate_path"),
            @Result(property = "flag", column = "flag"),
            @Result(property = "peerId", column = "peer_id"),
            @Result(property = "date", column = "date")
    })
    List<CA> listAll();

}
