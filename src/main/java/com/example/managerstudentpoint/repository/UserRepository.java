package com.example.managerstudentpoint.repository;

import com.example.managerstudentpoint.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByUserNameLike(String name);
}
