package com.example.managerstudentpoint.repository;

import com.example.managerstudentpoint.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Score, Long> {
//    List<Reports> findAllByUsersLike(User user);
}
