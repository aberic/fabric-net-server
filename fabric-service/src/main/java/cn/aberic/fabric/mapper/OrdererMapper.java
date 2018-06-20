package cn.aberic.fabric.mapper;

import cn.aberic.thrift.orderer.OrdererInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface OrdererMapper {

    @Insert("insert into orderer (name,location,org_id,date) values (#{o.name},#{o.location},#{o.orgId},#{o.date})")
    int add(@Param("o") OrdererInfo orderer);

    @Update("update orderer set name=#{o.name}, location=#{o.location} where rowid=#{o.id}")
    int update(@Param("o") OrdererInfo orderer);

    @Select("select count(name) from orderer where org_id=#{id}")
    int count(@Param("id") int id);

    @Select("select rowid,name,location,org_id,date from orderer where rowid=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "location", column = "location"),
            @Result(property = "org_id", column = "orgId"),
            @Result(property = "date", column = "date")
    })
    OrdererInfo get(@Param("id") int id);

    @Select("select rowid,name,location,org_id,date from orderer where org_id=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "location", column = "location"),
            @Result(property = "org_id", column = "orgId"),
            @Result(property = "date", column = "date")
    })
    List<OrdererInfo> list(@Param("id") int id);

    @Select("select rowid,name,location,org_id,date from orderer")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "location", column = "location"),
            @Result(property = "org_id", column = "orgId"),
            @Result(property = "date", column = "date")
    })
    List<OrdererInfo> listAll();

}
