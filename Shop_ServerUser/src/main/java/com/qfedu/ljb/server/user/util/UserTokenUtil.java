package com.qfedu.ljb.server.user.util;

import com.alibaba.fastjson.JSON;
import com.qfedu.common.model.LoginToken;

public class UserTokenUtil {
    public static LoginToken parseToken(String json){
        if (json != null){
            return JSON.parseObject(json, LoginToken.class);
        }else {
            return null;
        }
    }

    public static int parseTokenId(String json){
        if (json != null){
            LoginToken user = JSON.parseObject(json, LoginToken.class);
            return user.getUid();
        }else {
            return 0;
        }
    }
}
