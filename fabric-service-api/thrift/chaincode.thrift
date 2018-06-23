namespace java cn.aberic.thrift.chaincode

struct ChaincodeInfo {
    1:i32 id,
    2:required string name,
    3:required string path,
    4:required string version,
    5:required i32 proposalWaitTime,
    6:required i32 invokeWaitTime,
    7:required i32 channelId,
    8:optional string date,
    9:optional string channelName,
    10:optional string peerName,
    11:optional string orgName,
    12:optional string leagueName
}
service ChaincodeService {

    i32 add(1:ChaincodeInfo chaincodeInfo);

    i32 update(1:ChaincodeInfo chaincodeInfo);

    list<ChaincodeInfo> listAll();

    list<ChaincodeInfo> listById(1:i32 id);

    ChaincodeInfo get(1:i32 id);

    i32 countById(1:i32 id);

    i32 count();

}