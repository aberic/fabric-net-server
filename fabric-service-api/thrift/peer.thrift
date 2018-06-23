namespace java cn.aberic.thrift.peer

struct PeerInfo {
    1:i32 id,
    2:string name,
    3:string eventHubName,
    4:string location,
    5:string eventHubLocation,
    6:bool isEventListener,
    7:i32 orgId,
    8:string orgName,
    9:string date,
    10:i32 channelCount
}
service PeerService {

    i32 add(1:PeerInfo peerInfo);

    i32 update(1:PeerInfo peerInfo);

    list<PeerInfo> listAll();

    list<PeerInfo> listById(1:i32 id);

    PeerInfo get(1:i32 id);

    i32 countById(1:i32 id);

    i32 count();

}