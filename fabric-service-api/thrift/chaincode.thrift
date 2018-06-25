namespace java cn.aberic.thrift.chaincode

struct ChaincodeInfo {
    1:i32 id,
    2:required string name,
    3:optional string source,
    4:optional string path,
    5:required string version,
    6:required i32 proposalWaitTime,
    7:required i32 invokeWaitTime,
    8:required i32 channelId,
    9:optional string date,
    10:optional string channelName,
    11:optional string peerName,
    12:optional string orgName,
    13:optional string leagueName
}
service ChaincodeService {

    i32 add(1:ChaincodeInfo chaincodeInfo);

    string install(1:ChaincodeInfo chaincodeInfo, 2:binary buff, 3:string fileName);

    string instantiate(1:ChaincodeInfo chaincodeInfo, 2:list<string> strArray)

    i32 update(1:ChaincodeInfo chaincodeInfo);

    list<ChaincodeInfo> listAll();

    list<ChaincodeInfo> listById(1:i32 id);

    ChaincodeInfo get(1:i32 id);

    i32 countById(1:i32 id);

    i32 count();

}