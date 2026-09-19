package com.example.taskservice.controller;

import com.example.taskservice.dto.request.UserUpdatePasswordRequestDto;
import com.example.taskservice.dto.request.UserUpdateNameRequestDto;
import com.example.taskservice.dto.response.user.UserResponseSummaryDto;
import com.example.taskservice.dto.response.UserSummaryResponseDto;
import com.example.taskservice.dto.response.user.UserUpdatePasswordResponseDto;
import com.example.taskservice.dto.response.user.UserUploadProfilePictureResponseDto;
import com.example.taskservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public UserResponseSummaryDto getUser(@PathVariable UUID userId) {
        return userService.getUserInfo(userId);
    }

    @PatchMapping("/{userId}/name")
    @PreAuthorize("#userId == authentication.principal")
    public UserSummaryResponseDto updateNames(
            @PathVariable UUID userId,
            @Valid @RequestBody UserUpdateNameRequestDto userUpdateRequest
    ) {
        return userService.updateNames(userId, userUpdateRequest);
    }

    @GetMapping()
    public List<UserSummaryResponseDto> getAllUsers(){
        return userService.getAllUsers();
    }

    @PatchMapping("/{userId}/password")
    @PreAuthorize("#userId == authentication.principal")
    public UserUpdatePasswordResponseDto updatePassword(
            @PathVariable UUID userId,
            @Valid @RequestBody UserUpdatePasswordRequestDto userUpdateRequest
    ) {
        return userService.updatePassword(userId, userUpdateRequest);
    }

    @PutMapping("/{userId}/profile-picture")
    @PreAuthorize("#userId == authentication.principal")
    public UserUploadProfilePictureResponseDto uploadPhoto(
            @PathVariable UUID userId,
            @RequestBody byte[] file,
            @RequestHeader(
                    name = HttpHeaders.CONTENT_TYPE,
                    defaultValue = MediaType.APPLICATION_OCTET_STREAM_VALUE
            ) String contentType
    ) {
        return userService.uploadPhoto(userId, file, contentType);
    }

    @DeleteMapping("/{userId}/profile-picture")
    @PreAuthorize("#userId == authentication.principal")
    public ResponseEntity<Void> deleteProfilePicture(@PathVariable UUID userId) {
        userService.deleteProfilePicture(userId);
        return ResponseEntity.noContent().build();
    }
}
