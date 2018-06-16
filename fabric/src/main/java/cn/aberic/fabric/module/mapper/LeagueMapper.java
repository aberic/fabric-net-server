package cn.aberic.fabric.module.mapper;

import cn.aberic.fabric.module.bean.dto.LeagueDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface LeagueMapper {

    @Insert("insert into league values (#{l.name})")
    int addLeague(@Param("l") LeagueDTO league);

    @Update("update league set name=#{l.name} where rowid=#{l.id}")
    int updateLeagueById(@Param("l") LeagueDTO league);

    @Select("select rowid,name from league")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name")
    })
    List<LeagueDTO> getLeagueList();

}
