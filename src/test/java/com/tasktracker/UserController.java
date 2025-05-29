package com.tasktracker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasktracker.entity.User;
import com.tasktracker.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void cleanDb() {
        userRepository.deleteAll();
    }

    @Test
    void whenCreateUser_thenCanRetrieveById() throws Exception {
        // prepare JSON body
        String json = """
            {
              "username": "dave",
              "email": "dave@example.com",
              "password": "Secret123!"
            }
            """;

        // POST /api/users
        MvcResult postResult = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        // parse returned User
        User created = mapper.readValue(postResult.getResponse().getContentAsString(), User.class);
        assertThat(created.getId()).isNotNull();

        // GET /api/users/{id}
        mockMvc.perform(get("/api/users/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("dave"))
                .andExpect(jsonPath("$.email").value("dave@example.com"));


        Optional<User> fromDb = userRepository.findById(created.getId());
        assertThat(fromDb).isPresent().get().extracting(User::getUsername).isEqualTo("dave");
    }
}
