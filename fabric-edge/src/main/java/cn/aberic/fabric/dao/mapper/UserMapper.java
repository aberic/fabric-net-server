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

import cn.aberic.fabric.dao.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface UserMapper {

    @Insert("insert into user  (username,password) values (#{u.username},#{u.password})")
    int add(@Param("u")User user);

    @Update("update user set password=#{u.password} where username=#{u.username}")
    int update(@Param("u")User user);

    @Delete("delete from user where id=#{id}")
    int delete(@Param("id") int id);

    @Select("select id,username,password from user where username=#{username}")
    User get(@Param("username") String username);

    @Select("select id,username,date from user")
    List<User> listAll();

}
