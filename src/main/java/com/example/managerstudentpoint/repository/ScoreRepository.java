package com.example.managerstudentpoint.repository;

import com.example.managerstudentpoint.entity.Score;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    @Query(value = "select s from Score s join s.subject sub " +
            " where concat(sub.id, sub.nameSubject) like %:subject% and sub.status =:isDelete  ")
    Page<Score> findAllScoreBySubject(boolean isDelete,String subject, Pageable pageable);

    @Query("select s from Score s join s.users u" +
            " where s.users.isDelete=:isDelete and s.subject.id = :sub_id and s.users.groupClass.id =:c_id")
    List<Score> getScoresByGroupClassAndSubject(boolean isDelete,Long sub_id, Long c_id);

    @Query("select  s from Score s join s.users u " +
            " where s.users.isDelete =:isDelete and s.users.rollNumber =:rollNumber")
    List<Score> getScoresByUsers(boolean isDelete, String rollNumber);

    Score findAllByUsers_IdAndSubject_Id(Long sub_id, Long user_id);

}
