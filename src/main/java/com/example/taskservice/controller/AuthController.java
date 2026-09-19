package com.example.taskservice.controller;

import com.example.taskservice.dto.request.UserLoginRequestDto;
import com.example.taskservice.dto.request.UserRegisterRequestDto;
import com.example.taskservice.dto.response.UserLogInResponseDto;
import com.example.taskservice.dto.response.UserRegisterResponseDto;
import com.example.taskservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final UserService authService;

    @PostMapping("/register")
    public UserRegisterResponseDto register(@Valid @RequestBody UserRegisterRequestDto userCreateRequest){
        return authService.register(userCreateRequest);
    }

    @PostMapping("/login")
    public UserLogInResponseDto logIn(@Valid @RequestBody UserLoginRequestDto userCreateRequest){
        return authService.logIn(userCreateRequest);
    }
}
