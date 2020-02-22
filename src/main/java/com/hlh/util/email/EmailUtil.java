package com.hlh.util.email;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * 邮件工具类
 */

@Component
public class EmailUtil {

    private Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private JavaMailSender mailSender; //框架自带的

    @Value("${spring.mail.username}")  //发送人的邮箱
    private String from;

    /**
     * 发送普通邮件
     *
     * @param title 标题
     * @param url   内容
     * @param email 接收人
     */
    @Async  //意思是异步调用这个方法
    public void sendMail(String title, String url, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // 发送人的邮箱
        message.setSubject(title); //标题
        message.setTo(email); //发给谁  对方邮箱
        message.setText(url); //内容
        mailSender.send(message); //发送

        logger.info("/*邮箱发送成功->发送人:%s, 标题:%s, 接收人: %s, 内容: %s*/", from, title, email, url);
    }

    /**
     * 发送验证码邮件
     *
     * @param title 标题
     * @param code  验证码
     * @param email 接收人
     */
    @Async  //意思是异步调用这个方法
    public void sendCodeMail(String title, String code, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // 发送人的邮箱
        message.setSubject(title); //标题
        message.setTo(email); //发给谁  对方邮箱
        message.setText("你的验证码是：" + code + ",请在五分钟内完成验证服务!"); //内容
        mailSender.send(message); //发送

        logger.info("/*邮箱验证码发送成功->发送人:%s, 标题:%s, 接收人: %s, 验证码: %s*/", from, title, email, code);
    }
}
