/*
 * Copyright (c) 2018. Aberic - All Rights Reserved.
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

package cn.aberic.fabric.utils;

import cn.aberic.fabric.bean.Trace;
import cn.aberic.fabric.dao.Block;
import cn.aberic.fabric.dao.CA;
import cn.aberic.fabric.dao.Channel;
import cn.aberic.fabric.service.BlockService;
import cn.aberic.fabric.service.CAService;
import cn.aberic.fabric.service.ChannelService;
import cn.aberic.fabric.service.TraceService;
import cn.aberic.fabric.utils.pool.ThreadFNSPool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 作者：Aberic on 2018/8/11 00:26
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class BlockUtil {

    private static BlockUtil instance;

    private boolean isAlive = true;
    private boolean isInsert = false;

    private final List<Channel> channels = new LinkedList<>();
    private final List<Block> blocks = new LinkedList<>();

    public static BlockUtil obtain() {
        if (null == instance) {
            synchronized (BlockUtil.class) {
                if (null == instance) {
                    instance = new BlockUtil();
                }
            }
        }
        return instance;
    }

    public void checkChannel(ChannelService channelService, CAService caService, BlockService blockService, TraceService traceService, List<Channel> channels) {
        for (Channel channel : channels) {
            boolean hadOne = false;
            for (Channel channelTmp : this.channels) {
                if (channelTmp.getId() == channel.getId()) {
                    hadOne = true;
                }
            }
            if (!hadOne) {
                this.channels.add(channel);
                execChannel(channelService, caService, blockService, traceService, channel.getId());
            }
            CA ca = caService.listById(channel.getPeerId()).get(0);
            Trace trace = new Trace();
            trace.setChannelId(channel.getId());
            JSONObject blockMessage = JSON.parseObject(traceService.queryBlockInfoWithCa(trace, ca));
            int code = blockMessage.containsKey("code") ? blockMessage.getInteger("code") : 9999;
            if (code == 200) {
                channelService.updateHeight(channel.getId(), blockMessage.getJSONObject("data").getInteger("height"));
            }
        }
    }

    private void execChannel(ChannelService channelService, CAService caService, BlockService blockService, TraceService traceService, int channelId) {
        ThreadFNSPool.obtain().submitFixed(() -> {
            Block block = blockService.getByChannelId(channelId);
            int height = -1;
            if (null != block) {
                height = block.getHeight();
            }
            height = height == -1 ? 0 : height + 1;
            CA ca = caService.listById(channelService.get(channelId).getPeerId()).get(0);
            while (isAlive) {
                if (!isInsert) {
                    if (execBlock(blockService, traceService, channelId, height, ca)) {
                        height++;
                    } else {
                        synchronized (blocks) {
                            isInsert = true;
                            insert(blockService);
                        }
                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private boolean execBlock(BlockService blockService, TraceService traceService, int channelId, int height, CA ca) {
        try {
            Trace trace = new Trace();
            trace.setChannelId(channelId);
            trace.setTrace(String.valueOf(height));
            JSONObject blockMessage = JSON.parseObject(traceService.queryBlockByNumberWithCa(trace, ca));

            int code = blockMessage.containsKey("code") ? blockMessage.getInteger("code") : 9999;
            if (code != 200) {
                return false;
            }

            JSONArray envelopes = blockMessage.containsKey("data") ? blockMessage.getJSONObject("data").getJSONArray("envelopes") : new JSONArray();
            int txCount = 0;
            int rwSetCount = 0;
            int size = envelopes.size();
            for (int i = 0; i < size; i++) {
                JSONObject envelope = envelopes.getJSONObject(i);
                if (envelope.containsKey("transactionEnvelopeInfo")) {
                    txCount += envelope.getJSONObject("transactionEnvelopeInfo").getInteger("txCount");
                    JSONArray transactionActionInfoArray = envelope.getJSONObject("transactionEnvelopeInfo").getJSONArray("transactionActionInfoArray");
                    int transactionActionInfoArraySize = transactionActionInfoArray.size();
                    for (int j = 0; j < transactionActionInfoArraySize; j++) {
                        JSONObject transactionActionInfo = transactionActionInfoArray.getJSONObject(j);
                        rwSetCount += transactionActionInfo.getJSONObject("rwsetInfo").getInteger("nsRWsetCount");
                    }
                }
            }

            String timestamp = envelopes.getJSONObject(0).getString("timestamp");
            Date date = DateUtil.str2Date(timestamp, "yyyy/MM/dd HH:mm:ss");

            Block block = new Block();
            block.setChannelId(channelId);
            block.setHeight(height);
            block.setDataHash(blockMessage.getJSONObject("data").getString("dataHash"));
            block.setCalculatedHash(blockMessage.getJSONObject("data").getString("calculatedBlockHash"));
            block.setPreviousHash(blockMessage.getJSONObject("data").getString("previousHashID"));
            block.setEnvelopeCount(size);
            block.setTxCount(txCount);
            block.setRwSetCount(rwSetCount);
            block.setTimestamp(timestamp);
            block.setCalculateDate(Integer.valueOf(DateUtil.date2Str(date, "yyyyMMdd")));
            block.setCreateDate(DateUtil.getCurrent("yyyy/MM/dd HH:mm:ss"));

            synchronized (blocks) {
                blocks.add(block);
                if (blocks.size() >= 100) {
                    isInsert = true;
                    insert(blockService);
                    Thread.sleep(1000);
                }
            }
            log.info(String.format("exec block data for number %s", height));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void insert(BlockService blockService) {
        log.info(String.format("insert block data now blockList %s", blocks.size()));
        for (Block block : blocks) {
            blockService.add(block);
        }
        blocks.clear();
        isInsert = false;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
