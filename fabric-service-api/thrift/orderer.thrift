namespace java cn.aberic.thrift.orderer

struct OrdererInfo {
    1:i32 id,
    2:string name,
    3:string location,
    4:i32 orgId,
    5:string orgName,
    6:string date
}
service OrdererService {

    i32 add(1:OrdererInfo ordererInfo);

    i32 update(1:OrdererInfo ordererInfo);

    list<OrdererInfo> listAll();

    list<OrdererInfo> listById(1:i32 id);

    OrdererInfo get(1:i32 id);

    i32 countById(1:i32 id);

    i32 count();

}