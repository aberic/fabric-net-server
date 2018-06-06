package cn.aberic.simple.module.mapper;

import cn.aberic.simple.module.dto.OrdererDTO;
import cn.aberic.simple.module.dto.OrgDTO;
import cn.aberic.simple.module.dto.PeerDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/5 17:11】
 */
@Mapper
public interface SimpleMapper {

    @Insert("insert into org (org_name,tls,ca_tls,username,crypto_config_dir,channel_artifacts_dir,ca_name,ca_location,org_msp_id,org_domain_name,orderer_domain_name," +
            "channel_name,chaincode_name,chaincode_source,chaincode_path,chaincode_version,proposal_wait_time,invoke_wait_time) " +
            "values (#{o.orgName},#{o.tls},#{o.caTls},#{o.username},#{o.cryptoConfigDir},#{o.channelArtifactsDir},#{o.caName},#{o.caLocation},#{o.orgMSPID},#{o.orgDomainName}," +
            "#{o.ordererDomainName},#{o.channelName},#{o.chaincodeName},#{o.chaincodeSource},#{o.chaincodePath},#{o.chaincodeVersion},#{o.proposalWaitTime},#{o.invokeWaitTime})")
    int addOrg(@Param("o") OrgDTO org);

    @Select("select id,org_name,tls,ca_tls,username,crypto_config_dir,channel_artifacts_dir,ca_name,ca_location,org_msp_id,org_domain_name,orderer_domain_name," +
            "channel_name,chaincode_name,chaincode_source,chaincode_path,chaincode_version,proposal_wait_time,invoke_wait_time from org where id=#{id}")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "orgName",  column = "org_name"),
            @Result(property = "tls",  column = "tls"),
            @Result(property = "caTls",  column = "ca_tls"),
            @Result(property = "username",  column = "username"),
            @Result(property = "cryptoConfigDir",  column = "crypto_config_dir"),
            @Result(property = "channelArtifactsDir",  column = "channel_artifacts_dir"),
            @Result(property = "caName",  column = "ca_name"),
            @Result(property = "caLocation",  column = "ca_location"),
            @Result(property = "orgMSPID",  column = "org_msp_id"),
            @Result(property = "orgDomainName",  column = "org_domain_name"),
            @Result(property = "ordererDomainName",  column = "orderer_domain_name"),
            @Result(property = "channelName",  column = "channel_name"),
            @Result(property = "chaincodeName",  column = "chaincode_name"),
            @Result(property = "chaincodeSource",  column = "chaincode_source"),
            @Result(property = "chaincodePath",  column = "chaincode_path"),
            @Result(property = "chaincodeVersion",  column = "chaincode_version"),
            @Result(property = "proposalWaitTime",  column = "proposal_wait_time"),
            @Result(property = "invokeWaitTime",  column = "invoke_wait_time")
    })
    OrgDTO getOrgById(@Param("id") int id);

    @Insert("insert into orderer (org_id,name,location) values (#{o.orgId},#{o.name},#{o.location})")
    int addOrderer(@Param("o") OrdererDTO orderer);

    @Select("select id,org_id,name,location from orderer where org_id=#{orgId}")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "orgId",  column = "org_id"),
            @Result(property = "name",  column = "name"),
            @Result(property = "location",  column = "location")
    })
    List<OrdererDTO> getOrdererByOrgId(@Param("orgId") int orgId);

    @Insert("insert into peer (org_id,peer_name,peer_event_hub_name,peer_location,peer_event_hub_location,event_listener) " +
            "values (#{p.orgId},#{p.peerName},#{p.peerEventHubName},#{p.peerLocation},#{p.peerEventHubLocation},#{p.isEventListener})")
    int addPeer(@Param("p") PeerDTO peer);

    @Select("select id,org_id,peer_name,peer_event_hub_name,peer_location,peer_event_hub_location,event_listener from peer where org_id=#{orgId}")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "orgId",  column = "org_id"),
            @Result(property = "peerName",  column = "peer_name"),
            @Result(property = "peerEventHubName",  column = "peer_event_hub_name"),
            @Result(property = "peerLocation",  column = "peer_location"),
            @Result(property = "peerEventHubLocation",  column = "peer_event_hub_location"),
            @Result(property = "isEventListener",  column = "event_listener")
    })
    List<PeerDTO> getPeerByOrgId(@Param("orgId") int orgId);

}
