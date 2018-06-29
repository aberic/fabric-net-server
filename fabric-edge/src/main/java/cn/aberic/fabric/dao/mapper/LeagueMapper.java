package cn.aberic.fabric.dao.mapper;

import cn.aberic.fabric.dao.League;
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

    @Update("update league set name=#{l.name} where rowid=#{l.id}")
    int update(@Param("l") League league);

    @Delete("delete from org where rowid=#{id}")
    int delete(@Param("id") int id);

    @Select("select rowid,name,date from league where rowid=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "date", column = "date")
    })
    League get(@Param("id") int id);

    @Select("select rowid,name,date from league")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "date", column = "date")
    })
    List<League> listAll();

}
