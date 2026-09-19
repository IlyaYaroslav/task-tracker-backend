package com.example.taskservice.mapper;

import com.example.taskservice.dto.request.UserRegisterRequestDto;
import com.example.taskservice.dto.response.UserLogInResponseDto;
import com.example.taskservice.dto.response.UserRegisterResponseDto;
import com.example.taskservice.dto.response.user.UserResponseSummaryDto;
import com.example.taskservice.dto.response.UserSummaryResponseDto;
import com.example.taskservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", source = "name")
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "profilePictureObjectName", ignore = true)
    User toEntity(UserRegisterRequestDto userCreateRequestDto);

    UserRegisterResponseDto toRegisterResponseDto(User user, String token);

    @Mapping(target = "accessToken", source = "token")
    UserLogInResponseDto toLoginResponseDto(User user, String token);

    @Mapping(target = "profilePicturePresignedUrl", source = "profilePicturePresignedUrl")
    UserResponseSummaryDto toUserResponseSummaryDto(User user, String profilePicturePresignedUrl);

    List<UserSummaryResponseDto> toSummaryDto(List<User> users);
}
