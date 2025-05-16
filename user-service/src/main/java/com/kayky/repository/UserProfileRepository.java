package com.kayky.repository;


import com.kayky.domain.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    //    @EntityGraph(attributePaths = {"user","profile"})
    @EntityGraph(value = "UserProfile.fullDetails")
    List<UserProfile> findAll();
}