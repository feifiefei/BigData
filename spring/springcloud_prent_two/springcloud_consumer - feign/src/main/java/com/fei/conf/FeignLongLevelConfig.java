package com.itheima.conf;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/17 0017 17:03
 */
@Configuration
public class FeignLongLevelConfig {
    @Bean
    public Logger.Level config() {
        return Logger.Level.FULL;//打印日志的数据，请求响应所有的内容都要打印出来
    }
}
