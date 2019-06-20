package com.qfedu.ljb.server.user.controller;

import com.qfedu.common.vo.R;
import com.qfedu.ljb.server.user.service.UserSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserSignController {
    @Autowired
    private UserSignService userSignService;

    @PostMapping("/usersign/signsave.do")
    public R save(@RequestParam("token") String  token){
        return userSignService.saveSign(token);
    }

    @GetMapping("/usersign/queryday.do")
    public R queryByDays(@RequestParam("token") String token,@RequestParam("days") int days){
        return userSignService.queryByDays(token,days);
    }

    @GetMapping("/usersign/all.do")
    public R all(@RequestParam("token") String token){
        return userSignService.queryByUid(token);
    }

    @GetMapping("/usersign/single.do")
    public R single(@RequestParam("token") String token){
        return userSignService.querySingle(token);
    }
}
