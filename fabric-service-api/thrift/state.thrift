namespace java cn.aberic.thrift.state

struct StateInfo {
    1:i32 id,
    2:list<string> strArray
}
service StateService {

    string invoke(StateInfo stateInfo);

    string query(StateInfo stateInfo);

}