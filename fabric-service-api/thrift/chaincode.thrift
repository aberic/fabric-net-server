namespace java cn.aberic.thrift.chaincode

struct ChaincodeInfo {
    1:i32 id,
    2:string name,
    3:string path,
    4:string version,
    5:i32 proposalWaitTime,
    6:i32 invokeWaitTime,
    7:i32 channelId,
    8:string channelName,
    9:string date
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