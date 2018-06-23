namespace java cn.aberic.thrift.common

struct SystemInfo {
    1:double cpu,
    2:double memory,
    3:double swap
}
service SystemService {

    SystemInfo get();

    bool init();

    bool isInit();

}