// src/test/java/com/tasktracker/repository/UserRepositoryTest.java
package com.tasktracker;

import com.tasktracker.entity.User;
import com.tasktracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void whenSave_thenFindById() {
        User u = User.builder()
                .username("kri")
                .email("kri@example.com")
                .password("Pwd12345!")
                .build();

        User saved = userRepository.save(u);

        Optional<User> found = userRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("kri");
    }
}
