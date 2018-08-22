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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 作者：Aberic on 2018/6/27 21:15
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
@ToString
public class Channel {

    private int id; // required
    private String name; // required
    private boolean blockListener; // required
    private String callbackLocation; // required
    private int peerId; // required
    private int height;
    private String date; // optional
    private String peerName; // optional
    private String orgName; // optional
    private String leagueName; // optional
    private int chaincodeCount; // optional
}
