package com.tasktracker.repository;

import com.tasktracker.entity.User;
import com.tasktracker.service.UserService;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>  {

}
