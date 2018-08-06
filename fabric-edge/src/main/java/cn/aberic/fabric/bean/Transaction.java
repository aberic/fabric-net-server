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

package cn.aberic.fabric.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 作者：Aberic on 2018/6/23 10:25
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class Transaction {

    /** 序号，无实际意义 */
    private int index;
    /** 块高度 */
    private int num;
    private int txCount;
    private String channelName;
    private String calculatedBlockHash;
    private String date;

}
