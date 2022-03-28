package com.kuyth.mybatis.r2dbc.demo.controller;

import com.kuyth.mybatis.r2dbc.demo.service.UserInfoService;
import com.kuyth.mybatis.r2dbc.demo.vo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class UserController {

    private final UserInfoService userInfoService;

    @Autowired
    public UserController(
            UserInfoService userInfoService
    ) {
        this.userInfoService = userInfoService;
    }

    @RequestMapping(value = "getUserInfo/{id}")
    public Mono<ResponseEntity<UserInfo>> getUserInfo(
            @PathVariable(name = "id") String id
    ) {
        return userInfoService.getUserInfo(id).map(item -> ResponseEntity.ok().body(item));
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public Mono<ResponseEntity<?>> getUserInfo(
            @RequestBody UserInfo userInfo
    ) {
        return userInfoService.signUp(userInfo).map(item -> ResponseEntity.ok().body(item));
    }

}
