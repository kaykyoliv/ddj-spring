package com.kayky.mapper;

import com.kayky.domain.User;
import com.kayky.domain.UserProfile;
import com.kayky.response.UserProfileGetResponse;
import com.kayky.response.UserProfileUserGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {

    List<UserProfileGetResponse> toUserProfileGetResponse(List<UserProfile> userProfile);

    List<UserProfileUserGetResponse> toUserProfileGetResponseList(List<User> users);


}