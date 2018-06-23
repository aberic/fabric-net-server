namespace java cn.aberic.thrift.org

struct OrgInfo {
    1:i32 id,
    2:string name,
    3:bool tls,
    4:string username,
    5:string cryptoConfigDir,
    6:string mspId,
    7:string domainName,
    8:string ordererDomainName,
    9:i32 leagueId,
    10:string leagueName,
    11:string date,
    12:i32 peerCount,
    13:i32 ordererCount
}
service OrgService {

    i32 add(1:OrgInfo orgInfo, 2:binary buff, 3:string fileName);

    i32 update(1:OrgInfo orgInfo, 2:binary buff, 3:string fileName);

    list<OrgInfo> listAll();

    list<OrgInfo> listById(1:i32 id);

    OrgInfo get(1:i32 id);

    i32 countById(1:i32 id);

    i32 count();

}