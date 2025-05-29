package com.tasktracker.service;

import com.tasktracker.entity.User;
import com.tasktracker.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public List<User> listUsers(int page, int size) {
        return userRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }


    public User createUser(User user) {
        return userRepository.save(user);
    }
}
