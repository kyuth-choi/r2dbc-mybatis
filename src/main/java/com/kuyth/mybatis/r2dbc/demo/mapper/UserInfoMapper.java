package com.kuyth.mybatis.r2dbc.demo.mapper;

import com.kuyth.mybatis.r2dbc.demo.vo.UserInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface UserInfoMapper {

    Mono<UserInfo> selectUserInfoById(String accountId);

    Flux<UserInfo> selectUserInfoList();

    Mono<Integer> insertUserInfo(UserInfo userInfo);
}
