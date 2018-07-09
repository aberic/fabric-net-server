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

package cn.aberic.fabric.dao;

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
public class Chaincode {

    private int id; // required
    private String name; // required
    private String source; // optional
    private String path; // optional
    private String policy; // optional
    private String version; // required
    private int proposalWaitTime = 90000; // required
    private int channelId; // required
    private String date; // optional
    private String channelName; // optional
    private String peerName; // optional
    private String orgName; // optional
    private String leagueName; // optional
    private boolean open;
}
