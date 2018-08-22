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

import cn.aberic.fabric.dao.entity.Role;
import cn.aberic.fabric.dao.entity.User;
import cn.aberic.fabric.dao.provider.RoleDAOProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface RoleMapper {

    @Insert("insert into fns_role (id,name) values (#{r.id},#{r.name})")
    int add(@Param("r") Role role);

    @InsertProvider(type = RoleDAOProvider.class, method = "insertAll")
    int addList(@Param("list") List<Role> roles);

    @Select("select id,name from fns_role where id>1")
    List<Role> listRole();

    @Select("select id,name from fns_role where id=#{id}")
    Role getRoleById(@Param("id") int id);

}
