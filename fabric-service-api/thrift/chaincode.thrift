namespace java cn.aberic.thrift.chaincode

struct ChaincodeInfo {
    1:i32 id,
    2:required string name,
    3:optional string source,
    4:optional string path,
    5:optional string policy,
    6:required string version,
    7:required i32 proposalWaitTime,
    8:required i32 invokeWaitTime,
    9:required i32 channelId,
    10:optional string date,
    11:optional string channelName,
    12:optional string peerName,
    13:optional string orgName,
    14:optional string leagueName
}
service ChaincodeService {

    i32 add(1:ChaincodeInfo chaincodeInfo);

    string install(1:ChaincodeInfo chaincodeInfo, 2:binary sourceBuff, 3:binary policyBuff, 4:string fileName);

    string instantiate(1:ChaincodeInfo chaincodeInfo, 2:list<string> strArray)

    i32 update(1:ChaincodeInfo chaincodeInfo);

    list<ChaincodeInfo> listAll();

    list<ChaincodeInfo> listById(1:i32 id);

    ChaincodeInfo get(1:i32 id);

    i32 countById(1:i32 id);

    i32 count();

}