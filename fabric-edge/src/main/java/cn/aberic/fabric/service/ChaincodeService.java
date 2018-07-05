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

package cn.aberic.fabric.service;

import cn.aberic.fabric.bean.Api;
import cn.aberic.fabric.dao.Chaincode;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/27 22:14
 * 邮箱：abericyang@gmail.com
 */
public interface ChaincodeService {

    int add(Chaincode chaincode);

    JSONObject install(Chaincode chaincode, MultipartFile file, Api api, boolean init);

    JSONObject instantiate(Chaincode chaincode, List<String> strArray);

    JSONObject upgrade(Chaincode chaincode, MultipartFile file, Api api);

    int update(Chaincode chaincode);

    List<Chaincode> listAll();

    List<Chaincode> listById(int id);

    Chaincode get(int id);

    int countById(int id);

    int count();

    int delete(int id);

    int deleteAll(int channelId);

}
