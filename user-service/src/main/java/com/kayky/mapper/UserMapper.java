package com.kayky.mapper;

import com.kayky.annotation.EncodedMapping;
import com.kayky.domain.User;
import com.kayky.request.UserPostRequest;
import com.kayky.request.UserPutRequest;
import com.kayky.response.UserGetResponse;
import com.kayky.response.UserPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = PasswordEncoderMapper.class)
public interface UserMapper {

    @Mapping(target = "roles", constant = "USER")
    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    User toUser(UserPostRequest postRequest);

    User toUser(UserPutRequest request);

    UserPostResponse toUserPostResponse(User user);

    UserGetResponse toUserGetResponse(User user);

    List<UserGetResponse> toUserGetResponseList(List<User> users);

}