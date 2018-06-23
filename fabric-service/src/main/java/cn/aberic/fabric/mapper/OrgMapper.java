package cn.aberic.fabric.mapper;

import cn.aberic.thrift.org.OrgInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface OrgMapper {

    @Insert("insert into org (name,tls,username,crypto_config_dir,msp_id,domain_name,orderer_domain_name,league_id,date)" +
            "values (#{o.name},#{o.tls},#{o.username},#{o.cryptoConfigDir},#{o.mspId},#{o.domainName}," +
            "#{o.ordererDomainName},#{o.leagueId},#{o.date})")
    int add(@Param("o") OrgInfo org);

    @Update("update org set name=#{o.name}, tls=#{o.tls}, username=#{o.username}, crypto_config_dir=#{o.cryptoConfigDir}, " +
            "msp_id=#{o.mspId}, domain_name=#{o.domainName}, orderer_domain_name=#{o.ordererDomainName}, league_id=#{o.leagueId}" +
            " where rowid=#{o.id}")
    int update(@Param("o") OrgInfo org);

    @Select("select count(name) from org where league_id=#{id}")
    int count(@Param("id") int id);

    @Select("select count(name) from org")
    int countAll();

    @Select("select rowid,name,tls,username,crypto_config_dir,msp_id,domain_name,orderer_domain_name,league_id,date from org where rowid=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "tls", column = "tls"),
            @Result(property = "username", column = "username"),
            @Result(property = "cryptoConfigDir", column = "crypto_config_dir"),
            @Result(property = "mspId", column = "msp_id"),
            @Result(property = "domainName", column = "domain_name"),
            @Result(property = "ordererDomainName", column = "orderer_domain_name"),
            @Result(property = "leagueId", column = "league_id"),
            @Result(property = "date", column = "date")
    })
    OrgInfo get(@Param("id") int id);

    @Select("select rowid,name,tls,username,crypto_config_dir,msp_id,domain_name,orderer_domain_name,league_id,date from org where league_id=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "tls", column = "tls"),
            @Result(property = "username", column = "username"),
            @Result(property = "cryptoConfigDir", column = "crypto_config_dir"),
            @Result(property = "mspId", column = "msp_id"),
            @Result(property = "domainName", column = "domain_name"),
            @Result(property = "ordererDomainName", column = "orderer_domain_name"),
            @Result(property = "leagueId", column = "league_id"),
            @Result(property = "date", column = "date")
    })
    List<OrgInfo> list(@Param("id") int id);

    @Select("select rowid,name,tls,username,crypto_config_dir,msp_id,domain_name,orderer_domain_name,league_id,date from org")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "tls", column = "tls"),
            @Result(property = "username", column = "username"),
            @Result(property = "cryptoConfigDir", column = "crypto_config_dir"),
            @Result(property = "mspId", column = "msp_id"),
            @Result(property = "domainName", column = "domain_name"),
            @Result(property = "ordererDomainName", column = "orderer_domain_name"),
            @Result(property = "leagueId", column = "league_id"),
            @Result(property = "date", column = "date")
    })
    List<OrgInfo> listAll();

}
