package com.kayky.service;

import com.kayky.domain.Profile;
import com.kayky.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository repository;

    public List<Profile> findAll() {
        return repository.findAll();
    }

    public Profile save(Profile profile) {
        return repository.save(profile);
    }

}