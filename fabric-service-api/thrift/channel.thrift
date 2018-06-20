namespace java cn.aberic.thrift.channel

struct ChannelInfo {
    1:i32 id,
    2:string name,
    3:string eventHubName,
    4:string location,
    5:string eventHubLocation,
    6:bool isEventListener,
    7:i32 peerId,
    8:string peerName,
    9:string date,
    10:i32 chaincodeCount
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