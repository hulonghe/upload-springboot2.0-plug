package com.hlh.config;

import com.hlh.filter.AuthenticationFilter;
import com.hlh.filter.BodyReaderFilter;
import com.hlh.filter.CorsFilter;
import com.hlh.filter.SqlInjectFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * @ClassName: FilterCfg
 * @Deprecated: 过滤器配置
 * 配置方式一：
 * 1.在Filter类上加@WebFilter；
 * 2. Springboot入口Class上加@ServletComponentScan(basePackages = {"filter所在包名"})
 * 多个Filter都这样配置，只要这些Filter之间没有先后依赖关系。
 * 我这里刚好有两个Filter有先后顺序要求，看到除了@WebFilter注解还有@Order这个注解。
 * 经测试程序在本地是好的，部署到远程顺序就不能保证了。没有深纠是什么原因，知道还有第二种配置方法。
 * 配置方式二：
 * 添加FilterRegistrationBean，如下。
 * RequireLoginFilter用普通的@Component标记，它本身用到的依赖autowire进去即可。
 * 经测试第二种配置Filter的Order是能保证的，order值越小，Filter越早经过。
 * @Author: 胡隆河
 * @Date: 2020/2/22 12:08
 **/

@Configuration
public class FilterCfg {

    @Bean
    public Filter bodyReaderFilter0() {
        return new BodyReaderFilter();
    }

    @Bean
    public Filter sqlInjectFilter1() {
        return new SqlInjectFilter();
    }

    @Bean
    public Filter corsFilter2() {
        return new CorsFilter();
    }

    @Bean
    public Filter authenticationFilter99() {
        return new AuthenticationFilter();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean0() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(bodyReaderFilter0());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(0);//order的数值越小 则优先级越高
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean1() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(sqlInjectFilter1());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(1);//order的数值越小 则优先级越高
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean2() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(corsFilter2());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(2);//order的数值越小 则优先级越高
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean99() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(authenticationFilter99());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(99);//order的数值越小 则优先级越高
        return filterRegistrationBean;
    }
}
