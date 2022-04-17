package com.example.managerstudentpoint.repository;

import com.example.managerstudentpoint.entity.Reports;
import com.example.managerstudentpoint.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ReportRepository extends JpaRepository<Reports, Long> {
//    List<Reports> findAllByUsersLike(User user);
}
