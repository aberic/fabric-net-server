package cn.aberic.fabric.mapper;

import cn.aberic.thrift.chaincode.ChaincodeInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface ChaincodeMapper {

    @Insert("insert into chaincode (name,path,version,proposal_wait_time,invoke_wait_time,channel_id,date) values " +
            "(#{c.name},#{c.path},#{c.version},#{c.proposalWaitTime},#{c.invokeWaitTime},#{c.channelId},#{c.date})")
    int add(@Param("c") ChaincodeInfo chainCode);

    @Update("update chaincode set name=#{c.name}, path=#{c.path}, version=#{c.version}, " +
            "proposal_wait_time=#{c.proposalWaitTime}, invoke_wait_time=#{c.invokeWaitTime} where rowid=#{c.id}")
    int update(@Param("c") ChaincodeInfo chainCode);

    @Select("select count(name) from chaincode where channel_id=#{id}")
    int count(@Param("id") int id);

    @Select("select rowid,name,path,version,proposal_wait_time,invoke_wait_time,channel_id,date from chaincode where rowid=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "path", column = "path"),
            @Result(property = "version", column = "version"),
            @Result(property = "proposalWaitTime", column = "proposal_wait_time"),
            @Result(property = "invokeWaitTime", column = "invoke_wait_time"),
            @Result(property = "channelId", column = "channel_id"),
            @Result(property = "date", column = "date")
    })
    ChaincodeInfo get(@Param("id") int id);

    @Select("select rowid,name,path,version,proposal_wait_time,invoke_wait_time,channel_id,date from chaincode where channel_id=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "path", column = "path"),
            @Result(property = "version", column = "version"),
            @Result(property = "proposalWaitTime", column = "proposal_wait_time"),
            @Result(property = "invokeWaitTime", column = "invoke_wait_time"),
            @Result(property = "channelId", column = "channel_id"),
            @Result(property = "date", column = "date")
    })
    List<ChaincodeInfo> list(@Param("id") int id);

    @Select("select rowid,name,path,version,proposal_wait_time,invoke_wait_time,channel_id,date from chaincode")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "path", column = "path"),
            @Result(property = "version", column = "version"),
            @Result(property = "proposalWaitTime", column = "proposal_wait_time"),
            @Result(property = "invokeWaitTime", column = "invoke_wait_time"),
            @Result(property = "channelId", column = "channel_id"),
            @Result(property = "date", column = "date")
    })
    List<ChaincodeInfo> listAll();

}
