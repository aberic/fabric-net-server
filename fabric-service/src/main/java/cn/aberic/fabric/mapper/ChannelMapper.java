package cn.aberic.fabric.mapper;

import cn.aberic.thrift.channel.ChannelInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface ChannelMapper {

    @Insert("insert into channel (name,peer_id,date) values (#{c.name},#{c.peerId},#{c.date})")
    int add(@Param("c") ChannelInfo channel);

    @Update("update channel set name=#{c.name} where rowid=#{c.id}")
    int update(@Param("c") ChannelInfo channel);

    @Select("select count(name) from channel where peer_id=#{id}")
    int count(@Param("id") int id);

    @Select("select rowid,name,peer_id,date from channel where rowid=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "peerId", column = "peer_id"),
            @Result(property = "date", column = "date")
    })
    ChannelInfo get(@Param("id") int id);

    @Select("select rowid,name,peer_id,date from channel where peer_id=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "peerId", column = "peer_id"),
            @Result(property = "date", column = "date")
    })
    List<ChannelInfo> list(@Param("id") int id);

    @Select("select rowid,name,peer_id,date from channel")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "peerId", column = "peer_id"),
            @Result(property = "date", column = "date")
    })
    List<ChannelInfo> listAll();

}
