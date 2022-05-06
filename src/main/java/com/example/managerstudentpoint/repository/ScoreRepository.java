package com.example.managerstudentpoint.repository;

import com.example.managerstudentpoint.entity.Score;
import com.example.managerstudentpoint.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
//    @Query("select s from Score s join s.users st join s.subject sc " +
//            " where " +
//            "s.users.isDelete =:isDelete and " +
//            "sc.id =:key")
//    Page<Score> findAllBySubject_Id (Long key, Pageable pageable);
    Page<Score> findAllBySubject(Subject subject, Pageable pageable);
//    Score findByStudentAndAndCourse(User user, Subject subject);
//
//    List<Score> findAllByStudent(User user);

}
