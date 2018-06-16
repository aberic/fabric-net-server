package cn.aberic.fabric.module.mapper;

import cn.aberic.fabric.module.bean.dto.PeerDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface PeerMapper {

    @Insert("insert into peer (name,event_hub_name,location,event_hub_location,event_listener,org_id) " +
            "values (#{p.name},#{p.eventHubName},#{p.location},#{p.eventHubLocation},#{p.isEventListener},#{p.orgId})")
    int add(@Param("p") PeerDTO peer);

    @Update("update peer set name=#{p.name}, event_hub_name=#{p.eventHubName}, location=#{p.location}" +
            ", event_hub_location=#{p.eventHubLocation}, event_listener=#{p.isEventListener} where rowid=#{p.id}")
    int update(@Param("p") PeerDTO peer);

    @Select("select rowid,name,event_hub_name,location,event_hub_location,event_listener,org_id from peer where rowid=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "eventHubName", column = "event_hub_name"),
            @Result(property = "location", column = "location"),
            @Result(property = "eventHubLocation", column = "event_hub_location"),
            @Result(property = "isEventListener", column = "event_listener"),
            @Result(property = "orgId", column = "org_id")
    })
    PeerDTO get(@Param("id") int id);

    @Select("select rowid,name,event_hub_name,location,event_hub_location,event_listener,org_id from peer where org_id=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "eventHubName", column = "event_hub_name"),
            @Result(property = "location", column = "location"),
            @Result(property = "eventHubLocation", column = "event_hub_location"),
            @Result(property = "isEventListener", column = "event_listener"),
            @Result(property = "orgId", column = "org_id")
    })
    List<PeerDTO> list(@Param("id") int id);

}
