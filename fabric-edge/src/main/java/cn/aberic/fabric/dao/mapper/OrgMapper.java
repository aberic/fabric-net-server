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

import cn.aberic.fabric.dao.Org;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface OrgMapper {

    @Insert("insert into org (msp_id,tls,league_id,date)" +
            "values (#{o.mspId},#{o.tls},#{o.leagueId},#{o.date})")
    int add(@Param("o") Org org);

    @Update("update org set tls=#{o.tls}, msp_id=#{o.mspId}, league_id=#{o.leagueId}" +
            " where rowid=#{o.id}")
    int update(@Param("o") Org org);

    @Select("select count(msp_id) from org where league_id=#{id}")
    int count(@Param("id") int id);

    @Select("select count(msp_id) from org")
    int countAll();

    @Delete("delete from org where rowid=#{id}")
    int delete(@Param("id") int id);

    @Delete("delete from org where league_id=#{leagueId}")
    int deleteAll(@Param("leagueId") int leagueId);

    @Select("select rowid,tls,msp_id,league_id,date from org where rowid=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "tls", column = "tls"),
            @Result(property = "mspId", column = "msp_id"),
            @Result(property = "leagueId", column = "league_id"),
            @Result(property = "date", column = "date")
    })
    Org get(@Param("id") int id);

    @Select("select rowid,tls,msp_id,league_id,date from org where league_id=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "tls", column = "tls"),
            @Result(property = "mspId", column = "msp_id"),
            @Result(property = "leagueId", column = "league_id"),
            @Result(property = "date", column = "date")
    })
    List<Org> list(@Param("id") int id);

    @Select("select rowid,tls,msp_id,league_id,date from org")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "tls", column = "tls"),
            @Result(property = "mspId", column = "msp_id"),
            @Result(property = "leagueId", column = "league_id"),
            @Result(property = "date", column = "date")
    })
    List<Org> listAll();

}
