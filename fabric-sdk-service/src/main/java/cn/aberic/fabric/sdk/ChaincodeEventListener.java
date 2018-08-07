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

package cn.aberic.fabric.sdk;

import com.alibaba.fastjson.JSONObject;

/**
 * 作者：Aberic on 2018/8/6 23:05
 * 邮箱：abericyang@gmail.com
 */
public interface ChaincodeEventListener {

    /**
     * 收到一个chaincode事件。ChaincodeEventListener不应该存在太长时间，因为它们会占用线程资源。
     * @param handle 处理产生此事件的链码事件监听器的句柄
     * @param jsonObject blockEvent包含链码事件的块事件信息
     */
    void received(String handle, JSONObject jsonObject, String eventName, String chaincodeId, String txId);
}
