namespace java cn.aberic.thrift.league

struct LeagueInfo {
    1:i32 id,
    2:string name,
    3:string date,
    4:i32 orgCount
}
service LeagueService {

    i32 add(1:LeagueInfo leagueInfo);

    i32 update(1:LeagueInfo leagueInfo);

    list<LeagueInfo> listAll();

    LeagueInfo get(1:i32 id);

}