package cn.aberic.fabric.module.mapper;

import cn.aberic.fabric.module.bean.dto.ChainCodeDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface ChainCodeMapper {

    @Insert("insert into chaincode (name,path,version,proposal_wait_time,invoke_wait_time,channel_id)" +
            "values (#{c.name},#{c.path},#{c.version},#{c.proposalWaitTime},#{c.invokeWaitTime},#{c.channelId})")
    int add(@Param("c") ChainCodeDTO chainCode);

    @Update("update chaincode set name=#{c.name}, path=#{c.path}, version=#{c.version}, " +
            "proposal_wait_time=#{c.proposalWaitTime}, invoke_wait_time=#{c.invokeWaitTime} where hash=#{c.hash}")
    int update(@Param("c") ChainCodeDTO chainCode);

    @Select("select rowid,name,path,version,proposal_wait_time,invoke_wait_time,channel_id from chaincode where rowid=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "path", column = "path"),
            @Result(property = "version", column = "version"),
            @Result(property = "proposalWaitTime", column = "proposal_wait_time"),
            @Result(property = "invokeWaitTime", column = "invoke_wait_time"),
            @Result(property = "channelId", column = "channel_id")
    })
    ChainCodeDTO get(@Param("id") int id);

    @Select("select rowid,name,path,version,proposal_wait_time,invoke_wait_time,channel_id from chaincode where channel_id=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "path", column = "path"),
            @Result(property = "version", column = "version"),
            @Result(property = "proposalWaitTime", column = "proposal_wait_time"),
            @Result(property = "invokeWaitTime", column = "invoke_wait_time"),
            @Result(property = "channelId", column = "channel_id")
    })
    List<ChainCodeDTO> list(@Param("id") int id);

}
