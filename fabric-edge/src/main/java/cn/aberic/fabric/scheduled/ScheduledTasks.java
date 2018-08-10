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

import cn.aberic.fabric.service.*;
import cn.aberic.fabric.utils.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 描述：
 *
 * @author : Aberic 【2018-08-10 11:13】
 */
@Component
@Slf4j
public class ScheduledTasks {

    @Resource
    private LeagueService leagueService;
    @Resource
    private OrgService orgService;
    @Resource
    private OrdererService ordererService;
    @Resource
    private PeerService peerService;
    @Resource
    private CAService caService;
    @Resource
    private ChannelService channelService;
    @Resource
    private ChaincodeService chaincodeService;
    @Resource
    private AppService appService;
    @Resource
    private TraceService traceService;
    @Resource
    private BlockService blockService;

    //fixedDelay = x 表示当前方法执行完毕x ms后，Spring scheduling会再次调用该方法
    @Scheduled(fixedDelay = 15000)
    public void homeUpgrade() {
        log.debug("===============>>>>>>>>>>home upgrade start<<<<<<<<<<===============");
        DataUtil.obtain().home(leagueService, orgService, ordererService,
                peerService, caService, channelService, chaincodeService,
                appService, traceService, blockService);
        log.debug("===============>>>>>>>>>>home upgrade end<<<<<<<<<<===============");
    }

}
