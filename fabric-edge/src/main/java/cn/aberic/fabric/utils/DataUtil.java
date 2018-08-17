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

import cn.aberic.fabric.bean.*;
import cn.aberic.fabric.dao.Channel;
import cn.aberic.fabric.service.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Aberic on 2018/8/10 21:04
 * 邮箱：abericyang@gmail.com
 */
public class DataUtil {

    private static DataUtil instance;

    public static DataUtil obtain() {
        if (null == instance) {
            synchronized (DataUtil.class) {
                if (null == instance) {
                    instance = new DataUtil();
                }
            }
        }
        return instance;
    }

    public Home home(LeagueService leagueService, OrgService orgService, OrdererService ordererService,
                     PeerService peerService, CAService caService, ChannelService channelService,
                     ChaincodeService chaincodeService, AppService appService, BlockService blockService) {
        int leagueCount = leagueService.listAll().size();
        int orgCount = orgService.count();
        int ordererCount = ordererService.count();
        int peerCount = peerService.count();
        int caCount = caService.count();
        int channelCount = channelService.count();
        int chaincodeCount = chaincodeService.count();
        int appCount = appService.count();

        List<Channel> channels = channelService.listAll();
        List<Block> blocks = blocks(channels, peerService, blockService);
        List<cn.aberic.fabric.dao.Block> blockDaos = blockService.getLimit(6);
        List<ChannelPercent> channelPercents = blockService.getChannelPercents(channels);
        List<ChannelBlockList> channelBlockLists = blockService.getChannelBlockLists(channels);
        DayStatistics dayStatistics = blockService.getDayStatistics();
        Platform platform = blockService.getPlatform();

        Home home = new Home();
        home.setLeagueCount(leagueCount);
        home.setOrgCount(orgCount);
        home.setOrdererCount(ordererCount);
        home.setPeerCount(peerCount);
        home.setCaCount(caCount);
        home.setChannelCount(channelCount);
        home.setChaincodeCount(chaincodeCount);
        home.setAppCount(appCount);
        home.setChannels(channels);
        home.setBlocks(blocks);
        home.setChannelPercents(channelPercents);
        home.setChannelBlockLists(channelBlockLists);
        home.setBlockDaos(blockDaos);
        home.setDayStatistics(dayStatistics);
        home.setPlatform(platform);
        home.setDayBlocks(blockService.get20CountList());
        home.setDayTxs(blockService.get20TxCountList());
        home.setDayRWs(blockService.get20RWCountList());
        CacheUtil.putHome(home);
        return home;
    }

    private List<Block> blocks(List<Channel> channels, PeerService peerService, BlockService blockService) {
        List<Block> blocks = new ArrayList<>();
        for (Channel channel : channels) {
            cn.aberic.fabric.dao.Block blockDao = blockService.getByChannelId(channel.getId());
            if (null == blockDao) {
                continue;
            }
            double totalHeight = channel.getHeight() - 1;
            double nowHeight = blockDao.getHeight();
            double percent = nowHeight > totalHeight ? 2 : nowHeight/totalHeight;
            Block block = new Block();
            block.setNum((int)nowHeight + 1);
            block.setPeerName(peerService.get(channel.getPeerId()).getName());
            block.setChannelName(channel.getName());
            block.setCalculatedBlockHash(blockDao.getCalculatedHash());
            block.setDate(blockDao.getTimestamp());
            block.setPercent(percent == 2 ? 0 : percent);
            block.setPercentStr(percent == 2 ? "--" : String.valueOf((int)(percent * 100)) + "%");
            blocks.add(block);
        }
        blocks.sort((t1, t2) -> {
            try {
                long td1 = DateUtil.str2Date(t1.getDate(), "yyyy/MM/dd HH:mm:ss").getTime();
                long td2 = DateUtil.str2Date(t2.getDate(), "yyyy/MM/dd HH:mm:ss").getTime();
                return Math.toIntExact(td2 - td1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        });
        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).setIndex(i + 1);
        }
        return blocks;
    }

}
