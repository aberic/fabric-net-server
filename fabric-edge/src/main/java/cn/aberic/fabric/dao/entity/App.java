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
import com.gitee.sunchenbin.mybatis.actable.command.BaseModel;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 描述：
 *
 * @author : Aberic 【2018-07-05 17:02】
 */
@Setter
@Getter
@ToString
@Table(name = "fns_app")
public class App extends BaseModel {

    @Column(name = "id",type = MySqlTypeConstant.INT,length = 9,isKey = true,isAutoIncrement = true)
    private int id;
    @Column(name = "name",type = MySqlTypeConstant.VARCHAR,length = 32)
    private String name;
    @Column(name = "key",type = MySqlTypeConstant.VARCHAR,length = 32)
    private String key;
    @Column(name = "chaincode_id",type = MySqlTypeConstant.INT,length = 9)
    private int chaincodeId;
    @Column(name = "create_date",type = MySqlTypeConstant.VARCHAR,length = 20)
    private String createDate;
    @Column(name = "modify_date",type = MySqlTypeConstant.VARCHAR,length = 20)
    private String modifyDate;
    @Column(name = "active",type = MySqlTypeConstant.INT,length = 1)
    private boolean active;

}
