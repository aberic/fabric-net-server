namespace java cn.aberic.thrift.channel

struct ChannelInfo {
    1:i32 id,
    2:string name,
    3:i32 peerId,
    4:string peerName,
    5:string date,
    6:i32 chaincodeCount
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