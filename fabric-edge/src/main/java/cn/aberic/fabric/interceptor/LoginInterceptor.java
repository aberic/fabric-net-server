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

import cn.aberic.fabric.dao.Role;
import cn.aberic.fabric.dao.User;
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
        String token = (String) request.getSession().getAttribute("token");
        User user = CacheUtil.getUser(token);
        if (null == user) {
            response.sendRedirect("/login");
            return false;
        }
        String uri = request.getRequestURI();
        System.out.print(uri);
        switch (user.getRoleId()) {
            case Role.ADMIN:
                if (StringUtils.contains(uri, USER.substring(0, USER.length() -2))) {
                    response.sendRedirect("/index");
                    return false;
                }
                break;
            case Role.MEMBER:
                if (!StringUtils.equals(uri, INDEX)) {
                    response.sendRedirect("/index");
                    return false;
                }
                break;
        }
        return true;

    }
}
