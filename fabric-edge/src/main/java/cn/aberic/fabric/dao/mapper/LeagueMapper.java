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

import cn.aberic.fabric.dao.entity.League;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface LeagueMapper {

    @Insert("insert into league  (name,date) values (#{l.name},#{l.date})")
    int add(@Param("l") League league);

    @Update("update league set name=#{l.name} where id=#{l.id}")
    int update(@Param("l") League league);

    @Delete("delete from league where id=#{id}")
    int delete(@Param("id") int id);

    @Select("select id,name,date from league where id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "date", column = "date")
    })
    League get(@Param("id") int id);

    @Select("select id,name,date from league")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "date", column = "date")
    })
    List<League> listAll();

}
