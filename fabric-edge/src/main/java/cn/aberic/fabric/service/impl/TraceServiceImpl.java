/*
 * Copyright (c) 2018. Aberic - aberic@qq.com - All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.aberic.fabric.service.impl;

import cn.aberic.fabric.base.BaseService;
import cn.aberic.fabric.bean.Trace;
import cn.aberic.fabric.dao.CA;
import cn.aberic.fabric.dao.mapper.*;
import cn.aberic.fabric.sdk.FabricManager;
import cn.aberic.fabric.service.TraceService;
import cn.aberic.fabric.utils.FabricHelper;
import cn.aberic.fabric.utils.VerifyUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:03】
 */
@Service("traceService")
public class TraceServiceImpl implements TraceService, BaseService {

    @Resource
    private AppMapper appMapper;
    @Resource
    private OrgMapper orgMapper;
    @Resource
    private OrdererMapper ordererMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;

    @Override
    public String queryBlockByTransactionID(Trace trace, CA ca) {
        return traceByVerify(trace, TraceIntent.TRANSACTION, ca);
    }

    @Override
    public String queryBlockByHash(Trace trace, CA ca) {
        return traceByVerify(trace, TraceIntent.HASH, ca);
    }

    @Override
    public String queryBlockByNumber(Trace trace, CA ca) {
        return traceByVerify(trace, TraceIntent.NUMBER, ca);
    }

    @Override
    public String queryBlockChainInfo(int id, String key, CA ca) {
        Trace trace = new Trace();
        trace.setId(id);
        trace.setKey(key);
        return traceByVerify(trace, TraceIntent.INFO, ca);
    }

    @Override
    public String queryBlockByNumberForIndex(Trace trace) {
        return trace(trace, TraceIntent.NUMBER, new CA());
    }

    @Override
    public String queryBlockChainInfoForIndex(int id) {
        Trace trace = new Trace();
        trace.setId(id);
        return trace(trace, TraceIntent.INFO, new CA());
    }

    enum TraceIntent {
        TRANSACTION, HASH, NUMBER, INFO
    }

    private String traceByVerify(Trace trace, TraceIntent intent, CA ca) {
        if (VerifyUtil.unRequest(trace, chaincodeMapper, appMapper)) {
            return responseFail("app key is invalid");
        }
        return trace(trace, intent, ca);
    }

    private String trace(Trace trace, TraceIntent intent, CA ca) {
        Map<String, String> resultMap = null;
        try {
            FabricManager manager = FabricHelper.obtain().get(orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper,
                    ca, trace.getId());
            switch (intent) {
                case TRANSACTION:
                    resultMap = manager.queryBlockByTransactionID(trace.getTrace());
                    break;
                case HASH:
                    resultMap = manager.queryBlockByHash(Hex.decodeHex(trace.getTrace().toCharArray()));
                    break;
                case NUMBER:
                    resultMap = manager.queryBlockByNumber(Long.valueOf(trace.getTrace()));
                    break;
                case INFO:
                    resultMap = manager.getBlockchainInfo();
                    break;
            }
            return responseSuccess(JSONObject.parseObject(resultMap.get("data")));
        } catch (Exception e) {
            e.printStackTrace();
            return responseFail(String.format("Request failed： %s", e.getMessage()));
        }
    }
}
