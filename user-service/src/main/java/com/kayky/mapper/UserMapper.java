package com.kayky.mapper;

import com.kayky.annotation.EncodedMapping;
import com.kayky.domain.User;
import com.kayky.request.UserPostRequest;
import com.kayky.request.UserPutRequest;
import com.kayky.response.UserGetResponse;
import com.kayky.response.UserPostResponse;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = PasswordEncoderMapper.class)
public interface UserMapper {

    @Mapping(target = "roles", constant = "USER")
    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    User toUser(UserPostRequest postRequest);

    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    User toUser(UserPutRequest request);

    UserPostResponse toUserPostResponse(User user);

    UserGetResponse toUserGetResponse(User user);

    List<UserGetResponse> toUserGetResponseList(List<User> users);

    @Mapping(target = "password", source = "rawPassword", qualifiedBy = EncodedMapping.class)
    @Mapping(target = "roles", source = "savedUser.roles")
    @Mapping(target = "id", source = "userToUpdate.id")
    @Mapping(target = "firstName", source = "userToUpdate.firstName")
    @Mapping(target = "lastName", source = "userToUpdate.lastName")
    @Mapping(target = "email", source = "userToUpdate.email")
    User toUserWithPasswordAndRoles(User userToUpdate, String rawPassword, User savedUser);

    @AfterMapping
    default void setPasswordIfNull(@MappingTarget User user, String rawPassword, User savedUser) {
        if (rawPassword == null) {
            user.setPassword(savedUser.getPassword());
        }
    }

}