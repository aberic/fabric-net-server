package cn.aberic.fabric.module.mapper;

import cn.aberic.fabric.module.bean.dto.OrdererDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface OrdererMapper {

    @Insert("insert into orderer (name,location,org_id) values (#{o.name},#{o.location},#{o.orgId})")
    int add(@Param("o") OrdererDTO orderer);

    @Update("update orderer set name=#{o.name}, location=#{o.location} where rowid=#{o.id}")
    int update(@Param("o") OrdererDTO orderer);

    @Select("select rowid,name,location,org_id from orderer where rowid=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "location", column = "location"),
            @Result(property = "org_id", column = "orgId")
    })
    OrdererDTO get(@Param("hash") int id);

    @Select("select rowid,name,location,org_id from orderer where org_id=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "location", column = "location"),
            @Result(property = "org_id", column = "orgId")
    })
    List<OrdererDTO> list(@Param("id") int id);

}
