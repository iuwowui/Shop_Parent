package com.qfedu.ljb.api.login.controller;

import com.qfedu.common.config.ProjectConfig;
import com.qfedu.common.vo.R;
import com.qfedu.ljb.api.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    // 登录
    @PostMapping("/api/login/login.do")
    public R login(String phone, String password){
        return loginService.login(phone,password);
    }

    // 检查是否有效   token的传递 1.参数2,。消息头 一般用token用消息头来穿
    @GetMapping("/api/login/checklogin.do")
    public R check(HttpServletRequest request){
        return loginService.check(request.getHeader(ProjectConfig.TOKENHEAD));
    }

    //注销
    @GetMapping("/api/login/exit.do")
    public R exit(HttpServletRequest request){
        return loginService.exit(request.getHeader(ProjectConfig.TOKENHEAD));
    }

}
