package com.example.managerstudentpoint.repository;

import com.example.managerstudentpoint.common.ERole;
import com.example.managerstudentpoint.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
    boolean existsByName(String name);
}
