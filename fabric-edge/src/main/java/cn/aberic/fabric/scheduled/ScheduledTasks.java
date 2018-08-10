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

package cn.aberic.fabric.scheduled;

import cn.aberic.fabric.dao.Channel;
import cn.aberic.fabric.service.ChannelService;
import cn.aberic.fabric.service.PeerService;
import cn.aberic.fabric.service.TraceService;
import cn.aberic.fabric.utils.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述：
 *
 * @author : Aberic 【2018-08-10 11:13】
 */
@Component
@Slf4j
public class ScheduledTasks {

    @Resource
    private PeerService peerService;
    @Resource
    private ChannelService channelService;
    @Resource
    private TraceService traceService;

    //fixedDelay = x 表示当前方法执行完毕x ms后，Spring scheduling会再次调用该方法
    @Scheduled(fixedDelay = 15000)
    public void homeUpgrade() {
        List<Channel> channels = channelService.listAll();
        DataUtil.obtain().home(channels, peerService, traceService);
        log.info("===home upgrade===");
    }

}
