package com.kayky.repository;


import com.kayky.domain.User;
import com.kayky.domain.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    //    @EntityGraph(attributePaths = {"user","profile"})
    @EntityGraph(value = "UserProfile.fullDetails")
    List<UserProfile> findAll();

    @Query("SELECT up.user FROM UserProfile up WHERE up.profile.id = ?1")
    List<User> findAllUserByProfileId(Long id);
}