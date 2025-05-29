package com.tasktracker;
import com.tasktracker.entity.User;
import com.tasktracker.repository.UserRepository;
import com.tasktracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetAllUsers_thenReturnList() {
        List<User> mockUsers = List.of(
                new User(1L, "alice", "alice@example.com", "pass", LocalDateTime.now(), null, null),
                new User(2L, "bob", "bob@example.com", "pass", LocalDateTime.now(), null, null)
        );
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> result = userService.getAllUsers();

        assertThat(result).hasSize(2).containsExactlyElementsOf(mockUsers);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void whenGetUserById_existingId_thenReturnUser() {
        User u = new User(1L, "alice", "alice@example.com", "pass", LocalDateTime.now(), null, null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        Optional<User> result = userService.getUserById(1L);

        assertThat(result).isPresent().get().isEqualTo(u);
        verify(userRepository).findById(1L);
    }

    @Test
    void whenSaveUser_thenRepositorySaveCalled() {
        User toSave = new User(null, "charlie", "c@example.com", "pwd", null, null, null);
        User saved = new User(3L, "charlie", "c@example.com", "pwd", LocalDateTime.now(), null, null);
        when(userRepository.save(toSave)).thenReturn(saved);

        User result = userService.saveUser(toSave);

        assertThat(result.getId()).isEqualTo(3L);
        verify(userRepository).save(toSave);
    }
}