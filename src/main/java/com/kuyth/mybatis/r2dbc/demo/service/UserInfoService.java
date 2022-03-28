package com.kuyth.mybatis.r2dbc.demo.service;

import com.kuyth.mybatis.r2dbc.demo.mapper.UserInfoMapper;
import com.kuyth.mybatis.r2dbc.demo.vo.UserInfo;
import org.apache.ibatis.r2dbc.ReactiveSqlSession;
import org.apache.ibatis.r2dbc.ReactiveSqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class UserInfoService {

    private final UserInfoMapper userInfoMapper;

    public UserInfoService(@Qualifier("smartstoreReactiveSqlSessionFactory") ReactiveSqlSessionFactory reactiveSqlSessionFactory) {
        ReactiveSqlSession reactiveSqlSession = reactiveSqlSessionFactory.openSession();
        userInfoMapper = reactiveSqlSession.getMapper(UserInfoMapper.class);
    }

    public Mono<UserInfo> getUserInfo(String accountId) {
        return userInfoMapper.selectUserInfoById(accountId);
    }

    public Mono<Integer> signUp(UserInfo userInfo) {

        return userInfoMapper.insertUserInfo(userInfo);
    }

}
