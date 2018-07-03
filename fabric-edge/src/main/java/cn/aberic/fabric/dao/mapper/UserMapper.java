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

    @Select("select rowid,username,password from user where username=#{username}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password")
    })
    User get(@Param("username") String username);

    @Select("select rowid,username,password from user")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password")
    })
    List<User> listAll();

}
