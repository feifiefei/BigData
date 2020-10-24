package com.ithema.controller;

import com.ithema.pojo.QueryVo;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/13 0013 19:16
 */

@RestController //接收字符串类型的数据返回
@RequestMapping("/account") //返回数据的目录
public class AccountController {
    @GetMapping("/show01")
    public String accountShow(Integer uid, String username, Boolean isVIP) {
        return "接收到的参数" + uid + "--" + username + "--" + isVIP;
    }

    //show01?uid=8848&username=asdasds&isVIP=0
    @GetMapping("/show03/{out}/{in}/{money}")
    public String accountShow03(@PathVariable("out") String out, @PathVariable("in") String in, @PathVariable("money") String money) {
        return "接收到的参数" + out + "--" + in + "--" + money;
    }

    @GetMapping("/show05")
    public String accountShow05(QueryVo queryVo) {
        return "接收到的参数" + queryVo;
    }

    @GetMapping("/show06")
    public String accountShow06(QueryVo queryVo) {
        return "接收到的参数" + queryVo;
    }

    @GetMapping("/show07")
    public String accountShow07(QueryVo queryVo) {
        return "接收到的参数" + queryVo;
    }

    @GetMapping("/show08")
    public String accountShow08(@RequestHeader("User-Agent") String userAgent) {
        return "接收到的参数" + userAgent;
    }

}
