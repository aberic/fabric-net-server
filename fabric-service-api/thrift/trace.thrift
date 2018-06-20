namespace java cn.aberic.thrift.trace

struct TraceInfo {
    1:i32 id,
    2:string trace
}
service TraceService {

    string queryBlockByTransactionID(1:TraceInfo traceInfo);

    string queryBlockByHash(1:TraceInfo traceInfo);

    string queryBlockByNumber(1:TraceInfo traceInfo);

    string queryBlockChainInfo(1:i32 id);

}