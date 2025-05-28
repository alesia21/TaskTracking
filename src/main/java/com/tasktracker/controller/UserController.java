package com.tasktracker.controller;

import com.tasktracker.entity.Project;
import com.tasktracker.entity.User;
import com.tasktracker.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController//returing domains objects as JSON :domain objects :users,projects,tasks
@RequestMapping("/api/users")//base url path for all the endpoints in the user controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;//injected repository to perform CRUD operations on user

    @PostMapping
    public User createUser(@Valid @RequestBody User user){//validate and bind the incoming json to a user object
        return userRepository.save(user);//save the new users to the database and returns the saved entity with all the fields : id , name , timestamps.
    }

    @GetMapping("/{id}/projects")
    public List<Project> getProjectsForUser(@PathVariable Long id) {//getting the user id by the url path
        return userRepository.findById(id)//get the user by id or throw an exception user not found, then return project list
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getProjects();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {//getting the user  by id from the url path.
        return userRepository.findById(id)//fetch for the user and then return else throw user not found.
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    @GetMapping
    public List<User> getAllUsers(
            @RequestParam(defaultValue = "0") int page, //taken as default value 0 ,
            @RequestParam(defaultValue = "10") int size//page size default 10
    ) {
        //pageable holds pagination which includes settings like which page and how many users (in this case) per page.
        //Create a pageable object to request a specific page of data
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable).getContent();  // returning only the page of users and the  content when specifying page and size
    }



}