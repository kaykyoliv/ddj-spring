package com.kayky.service;

import com.kayky.domain.UserProfile;
import com.kayky.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository repository;

    public List<UserProfile> findAll() {
        return repository.findAll();
    }

}