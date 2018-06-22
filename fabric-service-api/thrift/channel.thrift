namespace java cn.aberic.thrift.channel

struct ChannelInfo {
    1:i32 id,
    2:required string name,
    3:required i32 peerId,
    4:optional string date,
    5:optional string peerName,
    6:optional string orgName,
    7:optional string leagueName,
    8:optional i32 chaincodeCount
}
service ChannelService {

    i32 add(1:ChannelInfo channelInfo);

    i32 update(1:ChannelInfo channelInfo);

    list<ChannelInfo> listAll();

    list<ChannelInfo> listById(1:i32 id);

    ChannelInfo get(1:i32 id);

    i32 countById(1:i32 id);

    i32 count();

}