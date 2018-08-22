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
 * 描述：区块本地记录详情
 *
 * @author : Aberic 【2018-08-10 15:55】
 */
@Setter
@Getter
@ToString
@Table(name = "fns_block")
public class Block {

    @Column(name = "id",type = MySqlTypeConstant.INT,length = 9,isKey = true,isAutoIncrement = true)
    private int id;
    @Column(name = "channel_id",type = MySqlTypeConstant.INT,length = 9)
    private int channelId;
    @Column(name = "height",type = MySqlTypeConstant.INT,length = 9)
    private int height;
    @Column(name = "data_hash",type = MySqlTypeConstant.VARCHAR,length = 256)
    private String dataHash;
    @Column(name = "calculated_hash",type = MySqlTypeConstant.VARCHAR,length = 256)
    private String calculatedHash;
    @Column(name = "previous_hash",type = MySqlTypeConstant.VARCHAR,length = 256)
    private String previousHash;
    @Column(name = "envelope_count",type = MySqlTypeConstant.INT,length = 4)
    private int envelopeCount;
    @Column(name = "tx_count",type = MySqlTypeConstant.INT,length = 5)
    private int txCount;
    @Column(name = "r_w_set_count",type = MySqlTypeConstant.INT,length = 5)
    private int rwSetCount;
    @Column(name = "timestamp",type = MySqlTypeConstant.VARCHAR,length = 32)
    private String timestamp;
    @Column(name = "calculate_date",type = MySqlTypeConstant.INT,length = 8)
    private int calculateDate;
    @Column(name = "create_date",type = MySqlTypeConstant.VARCHAR,length = 14)
    private String createDate;

    private String peerChannelName;

}
