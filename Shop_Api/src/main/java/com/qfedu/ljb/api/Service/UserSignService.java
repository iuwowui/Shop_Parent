package com.qfedu.ljb.api.Service;

import com.qfedu.common.vo.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserSignService {
    @PostMapping("/usersign/signsave.do")
    R save(@RequestParam("token") String token);

    @GetMapping("/usersign/queryday.do")
    public R queryByDays(@RequestParam("token") String token,@RequestParam("days") int days);

    @GetMapping("/usersign/all.do")
    public R all(@RequestParam("token") String token);

    @GetMapping("/usersign/single.do")
    public R single(@RequestParam("token") String token);
}
