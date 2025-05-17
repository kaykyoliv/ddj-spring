package com.kayky.service;

import com.kayky.commons.ProfileUtils;
import com.kayky.commons.UserProfileUtils;
import com.kayky.commons.UserUtils;
import com.kayky.domain.UserProfile;
import com.kayky.repository.UserProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserProfileServiceTest {

    @InjectMocks
    private UserProfileService service;

    @Mock
    private UserProfileRepository repository;
    private List<UserProfile> userProfileList;

    @InjectMocks
    private UserProfileUtils userProfileUtils;

    @Spy
    private UserUtils userUtils;

    @Spy
    private ProfileUtils profileUtils;


    @BeforeEach
    void init(){
        userProfileList = userProfileUtils.newUserProfileList();
    }

    @Test
    @DisplayName("findAll() returns a list with all user profiles")
    @Order(1)
    void findAll_returnsAllProfiles_WhenSuccessful(){
        BDDMockito.when(repository.findAll()).thenReturn(userProfileList);

        var userProfiles = service.findAll();

        Assertions.assertThat(userProfiles)
                .isNotNull()
                .hasSameElementsAs(userProfileList);

        userProfiles.forEach(userProfile -> Assertions.assertThat(userProfile).hasNoNullFieldsOrProperties());
    }


    @Test
    @DisplayName("findAllUsersByProfileId() returns a list of users for a given profile")
    @Order(2)
    void findAllUsersByProfileId_ReturnsAllUsersForGivenProfile_WhenSuccessful() {
        var profileId = 99L;

        var usersByProfile = this.userProfileList.stream()
                .filter(userProfile -> userProfile.getProfile().getId().equals(profileId))
                .map(UserProfile::getUser)
                .toList();

        BDDMockito.when(repository.findAllUserByProfileId(profileId)).thenReturn(usersByProfile);

        var users = service.findAllUserByProfileId(profileId);

        Assertions.assertThat(users).hasSize(1)
                .doesNotContainNull()
                .hasSameElementsAs(usersByProfile);

        users.forEach(user -> Assertions.assertThat(user).hasNoNullFieldsOrProperties());
    }
}
