package com.kuyth.mybatis.r2dbc.demo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@Setter
@Getter
@ToString
public class UserInfo {

    private BigInteger user_no;
    private String account_id;
    private String account_pw;
    private String user_name;
    private String address;
    private int age;

}
