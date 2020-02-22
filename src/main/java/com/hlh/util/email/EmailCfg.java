package com.hlh.util.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * 邮件配置
 */

@Configuration
public class EmailCfg {

    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.protocol}")
    private String protocol;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.default-encoding}")
    private String defaultEncoding;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;
    @Value("${spring.mail.properties.mail.smtp.ssl.enable}")
    private String enable;
    @Value("${spring.mail.properties.mail.debug}")
    private String debug;
    @Value("${spring.mail.properties.mail.smtp.ssl.socketFactory}")
    private String socketFactory;
    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private String timeout;

    @Bean
    public JavaMailSenderImpl JavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setUsername(username);//设置邮箱账户
        mailSender.setPassword(password);//设置授权码
        mailSender.setHost(host);
        mailSender.setDefaultEncoding(defaultEncoding);//编码
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", auth);//开启认证
        properties.setProperty("mail.debug", debug);//启用调试
        properties.setProperty("mail.smtp.timeout", timeout);//设置链接超时
        properties.setProperty("mail.smtp.port", Integer.toString(25));//设置端口
        properties.setProperty("mail.smtp.socketFactory.port", Integer.toString(port));//设置ssl端口
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.socketFactory.class", socketFactory);
        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }
}
