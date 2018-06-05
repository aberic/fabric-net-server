package cn.aberic.simple.module.mapper;

import cn.aberic.simple.module.dto.OrgDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/5 17:58】
 */
@Mapper
public interface OrgDao {

    @Insert("insert into org (org_name,tls,ca_tls,username,crypto_config_dir,channel_artifacts_dir,ca_name,ca_location,org_msp_id,org_domain_name,orderer_domain_name," +
            "channel_name,chaincode_name,chaincode_source,chaincode_path,chaincode_version,proposal_wait_time,invoke_wait_time) " +
            "values (#{o.orgName},#{o.tls},#{o.caTls},#{o.username},#{o.cryptoConfigDir},#{o.channelArtifactsDir},#{o.caName},#{o.caLocation},#{o.orgMSPID},#{o.orgDomainName}," +
            "#{o.ordererDomainName},#{o.channelName},#{o.chaincodeName},#{o.chaincodeSource},#{o.chaincodePath},#{o.chaincodeVersion},#{o.proposalWaitTime},#{o.invokeWaitTime})")
    void addOrg(@Param("o") OrgDTO org);

    @Select("select id,org_name,tls,ca_tls,username,crypto_config_dir,channel_artifacts_dir,ca_name,ca_location,org_msp_id,org_domain_name,orderer_domain_name,channel_name,chaincode_name,chaincode_source,chaincode_path,chaincode_version,proposal_wait_time,invoke_wait_time from org where id=#{id}")
    OrgDTO getOrgById(@Param("id") int id);
}
