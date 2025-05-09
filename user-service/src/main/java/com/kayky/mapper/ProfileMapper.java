package com.kayky.mapper;

import com.kayky.domain.Profile;
import com.kayky.request.ProfilePostRequest;
import com.kayky.response.ProfileGetResponse;
import com.kayky.response.ProfilePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {

    Profile toProfile(ProfilePostRequest postRequest);

    ProfilePostResponse toProfilePostResponse(Profile profile);

    ProfileGetResponse toProfileGetResponse(Profile profile);

    List<ProfileGetResponse> toProfileGetResponseList(List<Profile> profiles);

}