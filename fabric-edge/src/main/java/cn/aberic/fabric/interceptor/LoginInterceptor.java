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

package cn.aberic.fabric.interceptor;

import cn.aberic.fabric.utils.CacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginInterceptor implements HandlerInterceptor {

    String LEAGUE = "/league/*";
    String ORG = "/org/*";
    String ORDERER = "/orderer/*";
    String PEER = "/peer/*";
    String CHANNEL = "/channel/*";
    String CHAINCODE = "/chaincode/*";
    String APP = "/app/*";
    String CA = "/ca/*";
    String USER = "/user/*";
    String INDEX = "/index";

    /** 在请求被处理之前调用 */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 检查每个到来的请求对应的session域中是否有登录标识
        String uri = request.getRequestURI();
        System.out.print(uri);
        String token = (String) request.getSession().getAttribute("token");
        String username = (String) request.getSession().getAttribute("username");
        if (!StringUtils.equalsIgnoreCase(token, CacheUtil.getString(username))){
            response.sendRedirect("/login");
            return false;
        }
        return true;

    }
}
