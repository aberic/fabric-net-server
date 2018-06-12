package cn.aberic.simple.module.mapper;

import cn.aberic.simple.module.dto.OrdererDTO;
import cn.aberic.simple.module.dto.OrgDTO;
import cn.aberic.simple.module.dto.PeerDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface SimpleMapper {

    @Insert("insert into org (hash,org_name,tls,username,crypto_config_dir,org_msp_id,org_domain_name,orderer_domain_name," +
            "channel_name,chaincode_name,chaincode_path,chaincode_version,proposal_wait_time,invoke_wait_time) " +
            "values (#{o.hash},#{o.orgName},#{o.tls},#{o.username},#{o.cryptoConfigDir},#{o.orgMSPID},#{o.orgDomainName}," +
            "#{o.ordererDomainName},#{o.channelName},#{o.chaincodeName},#{o.chaincodePath},#{o.chaincodeVersion},#{o.proposalWaitTime},#{o.invokeWaitTime})")
    int addOrg(@Param("o") OrgDTO org);

    @Update("update org set hash=#{o.hash}, org_name=#{o.orgName}, tls=#{o.tls}, username=#{o.username}, crypto_config_dir=#{o.cryptoConfigDir}, org_msp_id=#{o.orgMSPID}" +
            ", org_domain_name=#{o.orgDomainName}, orderer_domain_name=#{o.ordererDomainName}, channel_name=#{o.channelName}, chaincode_name=#{o.chaincodeName}, chaincode_path=#{o.chaincodePath}" +
            ", chaincode_version=#{o.chaincodeVersion}, proposal_wait_time=#{o.proposalWaitTime}, invoke_wait_time=#{o.invokeWaitTime} where hash=#{o.hash}")
    int updateOrgByHash(@Param("o") OrgDTO org);

    @Select("select hash,org_name,tls,username,crypto_config_dir,org_msp_id,org_domain_name,orderer_domain_name," +
            "channel_name,chaincode_name,chaincode_path,chaincode_version,proposal_wait_time,invoke_wait_time from org where hash=#{hash}")
    @Results({
            @Result(property = "hash", column = "hash"),
            @Result(property = "orgName", column = "org_name"),
            @Result(property = "tls", column = "tls"),
            @Result(property = "username", column = "username"),
            @Result(property = "cryptoConfigDir", column = "crypto_config_dir"),
            @Result(property = "orgMSPID", column = "org_msp_id"),
            @Result(property = "orgDomainName", column = "org_domain_name"),
            @Result(property = "ordererDomainName", column = "orderer_domain_name"),
            @Result(property = "channelName", column = "channel_name"),
            @Result(property = "chaincodeName", column = "chaincode_name"),
            @Result(property = "chaincodePath", column = "chaincode_path"),
            @Result(property = "chaincodeVersion", column = "chaincode_version"),
            @Result(property = "proposalWaitTime", column = "proposal_wait_time"),
            @Result(property = "invokeWaitTime", column = "invoke_wait_time")
    })
    OrgDTO getOrgByHash(@Param("hash") String hash);

    @Select("select hash,org_name,tls,username,crypto_config_dir,org_msp_id,org_domain_name,orderer_domain_name," +
            "channel_name,chaincode_name,chaincode_path,chaincode_version,proposal_wait_time,invoke_wait_time from org")
    @Results({
            @Result(property = "hash", column = "hash"),
            @Result(property = "orgName", column = "org_name"),
            @Result(property = "tls", column = "tls"),
            @Result(property = "username", column = "username"),
            @Result(property = "cryptoConfigDir", column = "crypto_config_dir"),
            @Result(property = "orgMSPID", column = "org_msp_id"),
            @Result(property = "orgDomainName", column = "org_domain_name"),
            @Result(property = "ordererDomainName", column = "orderer_domain_name"),
            @Result(property = "channelName", column = "channel_name"),
            @Result(property = "chaincodeName", column = "chaincode_name"),
            @Result(property = "chaincodePath", column = "chaincode_path"),
            @Result(property = "chaincodeVersion", column = "chaincode_version"),
            @Result(property = "proposalWaitTime", column = "proposal_wait_time"),
            @Result(property = "invokeWaitTime", column = "invoke_wait_time")
    })
    List<OrgDTO> getOrgList();

    @Insert("insert into orderer (hash,org_hash,name,location) values (#{o.hash},#{o.orgHash},#{o.name},#{o.location})")
    int addOrderer(@Param("o") OrdererDTO orderer);

    @Update("update orderer set hash=#{o.hash}, org_hash=#{o.orgHash}, name=#{o.name}, location=#{o.location} where hash=#{o.hash}")
    int updateOrdererByHash(@Param("o") OrdererDTO orderer);

    @Select("select hash,org_hash,name,location from orderer where hash=#{hash}")
    @Results({
            @Result(property = "hash", column = "hash"),
            @Result(property = "orgHash", column = "org_hash"),
            @Result(property = "name", column = "name"),
            @Result(property = "location", column = "location")
    })
    OrdererDTO getOrdererByHash(@Param("hash") String hash);

    @Select("select hash,org_hash,name,location from orderer where org_hash=#{orgHash}")
    @Results({
            @Result(property = "hash", column = "hash"),
            @Result(property = "orgHash", column = "org_hash"),
            @Result(property = "name", column = "name"),
            @Result(property = "location", column = "location")
    })
    List<OrdererDTO> getOrdererListByOrgHash(@Param("orgHash") String orgHash);

    @Insert("insert into peer (hash,org_hash,peer_name,peer_event_hub_name,peer_location,peer_event_hub_location,event_listener) " +
            "values (#{p.hash},#{p.orgHash},#{p.peerName},#{p.peerEventHubName},#{p.peerLocation},#{p.peerEventHubLocation},#{p.isEventListener})")
    int addPeer(@Param("p") PeerDTO peer);

    @Update("update peer set hash=#{p.hash}, org_hash=#{p.orgHash}, peer_name=#{p.peerName}, peer_event_hub_name=#{p.peerEventHubName}, peer_location=#{p.peerLocation}" +
            ", peer_event_hub_location=#{p.peerEventHubLocation}, event_listener=#{p.isEventListener} where hash=#{p.hash}")
    int updatePeerByHash(@Param("p") PeerDTO peer);

    @Select("select hash,org_hash,peer_name,peer_event_hub_name,peer_location,peer_event_hub_location,event_listener from peer where hash=#{hash}")
    @Results({
            @Result(property = "hash", column = "hash"),
            @Result(property = "orgHash", column = "org_hash"),
            @Result(property = "peerName", column = "peer_name"),
            @Result(property = "peerEventHubName", column = "peer_event_hub_name"),
            @Result(property = "peerLocation", column = "peer_location"),
            @Result(property = "peerEventHubLocation", column = "peer_event_hub_location"),
            @Result(property = "isEventListener", column = "event_listener")
    })
    PeerDTO getPeerByHash(@Param("hash") String hash);

    @Select("select hash,org_hash,peer_name,peer_event_hub_name,peer_location,peer_event_hub_location,event_listener from peer where org_hash=#{orgHash}")
    @Results({
            @Result(property = "hash", column = "hash"),
            @Result(property = "orgHash", column = "org_hash"),
            @Result(property = "peerName", column = "peer_name"),
            @Result(property = "peerEventHubName", column = "peer_event_hub_name"),
            @Result(property = "peerLocation", column = "peer_location"),
            @Result(property = "peerEventHubLocation", column = "peer_event_hub_location"),
            @Result(property = "isEventListener", column = "event_listener")
    })
    List<PeerDTO> getPeerListByOrgHash(@Param("orgHash") String orgHash);

}
