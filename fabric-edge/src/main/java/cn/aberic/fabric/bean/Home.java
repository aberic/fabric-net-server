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

package cn.aberic.fabric.bean;

import cn.aberic.fabric.dao.entity.Channel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 作者：Aberic on 2018/8/10 21:29
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class Home {

    int leagueCount;
    int orgCount;
    int ordererCount;
    int peerCount;
    int caCount;
    int channelCount;
    int chaincodeCount;
    int appCount;
    List<Channel> channels;
    List<Block> blocks;
    List<ChannelPercent> channelPercents;
    List<ChannelBlockList> channelBlockLists;
    List<cn.aberic.fabric.dao.entity.Block> blockDaos;
    DayStatistics dayStatistics;
    Platform platform;
    Curve dayBlocks;
    Curve dayTxs;
    Curve dayRWs;
}
