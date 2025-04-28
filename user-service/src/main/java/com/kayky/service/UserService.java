package com.kayky.service;

import com.kayky.domain.User;
import com.kayky.exception.NotFoundException;
import com.kayky.repository.UserHardCodedRepository;
import com.kayky.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
 @RequiredArgsConstructor
 public class UserService {
     private final UserHardCodedRepository repository;
     private final UserRepository userRepository;

     public List<User> findAll(String firstName) {
         return firstName == null ? userRepository.findAll() : repository.findByFirstName(firstName);
     }

    public User findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not Found"));
    }

    public User save(User user) {
        return repository.save(user);
    }

    public void delete(Long id) {
        var user = findByIdOrThrowNotFound(id);
        repository.delete(user);
    }

    public void update(User userToUpdate) {
        assertUserExists(userToUpdate.getId());
        repository.update(userToUpdate);
    }

    public void assertUserExists(Long id) {
        findByIdOrThrowNotFound(id);
    }
 }