package cn.aberic.fabric.dao.mapper;

import cn.aberic.fabric.dao.User;
import org.apache.ibatis.annotations.*;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface UserMapper {

    @Insert("insert into user  (username,password) values (#{u.username},#{u.password})")
    int add(@Param("u")User user);

    @Select("select rowid,username,password from user where username=#{username}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "date", column = "date")
    })
    User get(@Param("username") String username);

}
