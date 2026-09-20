package com.example.taskservice.it.controller;

import com.example.taskservice.controller.AuthController;
import com.example.taskservice.dto.request.UserRegisterRequestDto;
import com.example.taskservice.it.BaseIt;
import com.example.taskservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.client.RestTestClient;

public class AuthControllerTestIt extends BaseIt {

    @Autowired
    AuthController authController;

    @Autowired
    UserService userService;

    @Autowired
    RestTestClient restTestClient;

    @Test
    void register_success() {
        var request = UserRegisterRequestDto.builder()
                .name("Yaro")
                .password("123456")
                .email("asd@gmail.com")
                .build();

        var a = restTestClient.post()
                .uri("/api/v1/auth/register")
                .body(request)
                .exchange()
                .expectStatus().isOk();
        var sa = "a";
    }
}

