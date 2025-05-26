package com.tasktracker.service;

import com.tasktracker.entity.User;
import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers(int page, int size);
}
