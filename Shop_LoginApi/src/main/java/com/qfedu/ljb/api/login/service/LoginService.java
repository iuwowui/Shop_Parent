package com.qfedu.ljb.api.login.service;

import com.qfedu.common.vo.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "LoginProvider")

public interface LoginService {

    // 登录
    @PostMapping("/login/login.do")
    R login(@RequestParam("phone") String phone,@RequestParam("password") String password);

    // 检查是否有效   token的传递 1.参数2,。消息头 一般用token用消息头来穿
    @GetMapping("/login/checklogin.do")
    R check(@RequestParam("token") String token);

    //注销
    @GetMapping("/login/exit.do")
    R exit(@RequestParam("token") String token);
}
