package com.qfedu.ljb.entity;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
public class UserSign {
    private BigInteger id;
    private int uid;
    private int score;
    private int extrascore;
    private int day;
    private Date createtime;

}
