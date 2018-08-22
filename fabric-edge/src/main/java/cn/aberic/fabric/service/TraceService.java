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

package cn.aberic.fabric.service;

import cn.aberic.fabric.bean.Trace;
import cn.aberic.fabric.dao.entity.CA;

/**
 * 作者：Aberic on 2018/6/27 21:59
 * 邮箱：abericyang@gmail.com
 */
public interface TraceService {

    String queryBlockByTransactionID(Trace trace);

    String queryBlockByHash(Trace trace);

    String queryBlockByNumber(Trace trace);

    String queryBlockChainInfo(String cc, String key, CA ca);

    String queryBlockByNumberForIndex(Trace trace);

    String queryBlockChainInfoForIndex(int channelId);

    String queryBlockByNumberWithCa(Trace trace, CA ca);

    String queryBlockInfoWithCa(Trace trace, CA ca);
}
