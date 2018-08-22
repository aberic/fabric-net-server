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

@Setter
@Getter
public class Api {

    /** API 意图 */
    public enum Intent {
        INVOKE(1, "state/invoke"),
        QUERY(2, "state/query"),
        INFO(3, "trace/info"),
        HASH(4, "trace/hash"),
        NUMBER(5, "trace/number"),
        TXID(6, "trace/txid"),
        INSTANTIATE(7, "chaincode/instantiate"),
        UPGRADE(8, "chaincode/upgrade");

        private int index;
        private String apiUrl;

        Intent(int index, String apiUrl) {
            this.index = index;
            this.apiUrl = apiUrl;
        }

        public static Intent get(int index) {
            for (Intent i : Intent.values()) {
                if (i.getIndex() == index) {
                    return i;
                }
            }
            return null;
        }

        public String getApiUrl() {
            return apiUrl;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    /** 接口名称 */
    private String name = "";
    /** 接口意图 */
    private int index = 0;
    /** CA 标志*/
    private String flag = "";
    /** 请求app appKey */
    private String key = "";
    /** 请求Fabric版本号 */
    private String version = "";
    /** 接口执行参数 */
    private String exec = "";

    public Api() {
    }

    public Api(String name, int index) {
        this.name = name;
        this.index = index;
    }
}
