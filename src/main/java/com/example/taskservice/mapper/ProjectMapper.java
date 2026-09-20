package com.example.taskservice.mapper;

import com.example.taskservice.dto.request.ProjectCreateRequestDto;
import com.example.taskservice.dto.response.ProjectCreateResponseDto;
import com.example.taskservice.dto.response.ProjectResponseDto;
import com.example.taskservice.dto.response.ProjectSummaryResponseDto;
import com.example.taskservice.entity.Project;
import com.example.taskservice.model.ProjectMemberDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectMapper {


}
