package com.nebarrow.weathertracker.mapper;

import com.nebarrow.weathertracker.dto.request.PostUserRequest;
import com.nebarrow.weathertracker.dto.response.GetUserResponse;
import com.nebarrow.weathertracker.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(PostUserRequest postUserRequest);

    GetUserResponse toDto(User user);
}
