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

package cn.aberic.fabric.service;

import cn.aberic.fabric.dao.CA;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 作者：Aberic on 2018/7/12 21:11
 * 邮箱：abericyang@gmail.com
 */
public interface CAService {

    int add(CA ca, MultipartFile skFile, MultipartFile certificateFile);

    int update(CA ca, MultipartFile skFile, MultipartFile certificateFile);

    List<CA> listAll();

    List<CA> listById(int id);

    CA get(int id);

    CA getByFlag(String flag);

    int countById(int id);

    int count();

    int delete(int id);
}
