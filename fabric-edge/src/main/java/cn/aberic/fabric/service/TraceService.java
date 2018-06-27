package cn.aberic.fabric.service;

import cn.aberic.fabric.bean.Trace;

/**
 * 作者：Aberic on 2018/6/27 21:59
 * 邮箱：abericyang@gmail.com
 */
public interface TraceService {

    String queryBlockByTransactionID(Trace trace);

    String queryBlockByHash(Trace trace);

    String queryBlockByNumber(Trace trace);

    String queryBlockChainInfo(int id);
}
