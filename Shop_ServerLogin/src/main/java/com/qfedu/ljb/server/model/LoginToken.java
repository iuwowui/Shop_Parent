package com.qfedu.ljb.server.model;

import lombok.Data;

@Data
public class LoginToken {
    private String id;
    private String phone;
    private int uid;
}
