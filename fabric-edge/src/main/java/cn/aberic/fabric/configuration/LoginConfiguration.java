package cn.aberic.fabric.configuration;

import cn.aberic.fabric.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        LoginInterceptor loginInterceptor = new LoginInterceptor();
        InterceptorRegistration loginRegistry = registry.addInterceptor(loginInterceptor);
        // 拦截路径
        loginRegistry.addPathPatterns("/league/*");
        loginRegistry.addPathPatterns("/org/*");
        loginRegistry.addPathPatterns("/orderer/*");
        loginRegistry.addPathPatterns("/peer/*");
        loginRegistry.addPathPatterns("/channel/*");
        loginRegistry.addPathPatterns("/chaincode/*");
        loginRegistry.addPathPatterns("/state/*");
        loginRegistry.addPathPatterns("/trace/*");
        loginRegistry.addPathPatterns("/index");

    }

}
