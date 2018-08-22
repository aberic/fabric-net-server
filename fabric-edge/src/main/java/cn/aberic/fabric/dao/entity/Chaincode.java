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

package cn.aberic.fabric.dao.entity;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 作者：Aberic on 2018/6/27 21:16
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
@ToString
@Table(name = "fns_chaincode")
public class Chaincode {

    @Column(name = "id",type = MySqlTypeConstant.INT,length = 9,isKey = true,isAutoIncrement = true)
    private int id; // required
    @Column(name = "name",type = MySqlTypeConstant.VARCHAR,length = 32)
    private String name; // required
    @Column(name = "source",type = MySqlTypeConstant.VARCHAR,length = 128)
    private String source; // optional
    @Column(name = "path",type = MySqlTypeConstant.VARCHAR,length = 128)
    private String path; // optional
    @Column(name = "policy",type = MySqlTypeConstant.VARCHAR,length = 128)
    private String policy; // optional
    @Column(name = "version",type = MySqlTypeConstant.VARCHAR,length = 45)
    private String version; // required
    @Column(name = "proposal_wait_time",type = MySqlTypeConstant.INT,length = 9)
    private int proposalWaitTime = 90000; // required
    @Column(name = "channel_id",type = MySqlTypeConstant.INT,length = 9)
    private int channelId; // required
    @Column(name = "cc",type = MySqlTypeConstant.VARCHAR,length = 128)
    private String cc; // optional
    @Column(name = "chaincode_event_listener",type = MySqlTypeConstant.INT,length = 1)
    private boolean chaincodeEventListener; // required
    @Column(name = "callback_location",type = MySqlTypeConstant.VARCHAR,length = 128)
    private String callbackLocation; // required
    @Column(name = "events",type = MySqlTypeConstant.VARCHAR,length = 512)
    private String events;
    @Column(name = "date",type = MySqlTypeConstant.VARCHAR,length = 14)
    private String date; // optional

    private String flag;
    private String channelName; // optional
    private String peerName; // optional
    private String orgName; // optional
    private String leagueName; // optional

}
