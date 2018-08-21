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

package cn.aberic.fabric.dao;

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
public class Block {

    private int id;
    private int channelId;
    private String peerChannelName;
    private int height;
    private String dataHash;
    private String calculatedHash;
    private String previousHash;
    private int envelopeCount;
    private int txCount;
    private int rwSetCount;
    private String timestamp;
    private int calculateDate;
    private String createDate;

}
