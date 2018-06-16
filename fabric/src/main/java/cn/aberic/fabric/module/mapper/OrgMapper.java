package cn.aberic.fabric.module.mapper;

import cn.aberic.fabric.module.bean.dto.OrgDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface OrgMapper {

    @Insert("insert into org (name,tls,username,crypto_config_dir,msp_id,domain_name,orderer_domain_name,league_id)" +
            "values (#{o.name},#{o.tls},#{o.username},#{o.cryptoConfigDir},#{o.mspId},#{o.domainName}," +
            "#{o.ordererDomainName},#{o.leagueId})")
    int add(@Param("o") OrgDTO org);

    @Update("update org set name=#{o.name}, tls=#{o.tls}, username=#{o.username}, crypto_config_dir=#{o.cryptoConfigDir}, " +
            "msp_id=#{o.mspId}, domain_name=#{o.domainName}, orderer_domain_name=#{o.ordererDomainName}" +
            " where rowid=#{o.id}")
    int update(@Param("o") OrgDTO org);

    @Select("select rowid,name,tls,username,crypto_config_dir,msp_id,domain_name,orderer_domain_name,league_id from org where rowid=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "tls", column = "tls"),
            @Result(property = "username", column = "username"),
            @Result(property = "cryptoConfigDir", column = "crypto_config_dir"),
            @Result(property = "mspId", column = "msp_id"),
            @Result(property = "domainName", column = "domain_name"),
            @Result(property = "ordererDomainName", column = "orderer_domain_name"),
            @Result(property = "leagueId", column = "league_id")
    })
    OrgDTO get(@Param("id") int id);

    @Select("select rowid,name,tls,username,crypto_config_dir,msp_id,domain_name,orderer_domain_name,league_id from org where league_id=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "tls", column = "tls"),
            @Result(property = "username", column = "username"),
            @Result(property = "cryptoConfigDir", column = "crypto_config_dir"),
            @Result(property = "mspId", column = "msp_id"),
            @Result(property = "domainName", column = "domain_name"),
            @Result(property = "ordererDomainName", column = "orderer_domain_name"),
            @Result(property = "leagueId", column = "league_id")
    })
    List<OrgDTO> list(@Param("id") int id);

}
