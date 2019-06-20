package com.qfedu.ljb.api.controller;

import com.qfedu.common.config.ProjectConfig;
import com.qfedu.common.vo.R;
import com.qfedu.ljb.api.Service.UserSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserSignController {
    @Autowired
    private UserSignService userSignService;

    @PostMapping("/api/usersign/signsave.do")
    R save(HttpServletRequest request){
        return userSignService.save(request.getHeader(ProjectConfig.TOKENHEAD));
    }

    @GetMapping("/api/usersign/queryday.do")
    public R queryByDays(HttpServletRequest request,@RequestParam("days") int days){
        return userSignService.queryByDays(request.getHeader(ProjectConfig.TOKENHEAD),days);
    }

    @GetMapping("/api/usersign/all.do")
    public R all(HttpServletRequest request){
        return userSignService.all(request.getHeader(ProjectConfig.TOKENHEAD));
    }

    @GetMapping("/api/usersign/single.do")
    public R single(HttpServletRequest request){
        return userSignService.single(request.getHeader(ProjectConfig.TOKENHEAD));
    }
}
