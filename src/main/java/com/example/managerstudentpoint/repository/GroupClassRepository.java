package com.example.managerstudentpoint.repository;

import com.example.managerstudentpoint.entity.GroupClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupClassRepository extends JpaRepository<GroupClass, Long> {
    boolean existsByClassName(String username);
    Optional<GroupClass> findByClassName(String name);
//    GroupClass existsByClassName(String name);

}
