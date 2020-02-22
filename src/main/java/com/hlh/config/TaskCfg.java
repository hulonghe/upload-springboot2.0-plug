package com.hlh.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务配置
 */

@Component
@Configuration      //主要用于标记配置类，兼备Component的效果。
@EnableScheduling   //开启定时任务
public class TaskCfg {

    /**
     * 每日00:01分执行
     */
    @Scheduled(cron = "0 1 0 * * ?")
    //@Scheduled(fixedRate=5000)    //延时
    private void dayTask() {

    }
}