package com.example.managerstudentpoint.repository;

import com.example.managerstudentpoint.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query(value = "SELECT s FROM Subject s " +
            " where (concat( s.nameSubject, s.id ) like %:key%) and (:status = s.status) ")
    Page<Subject> getSubjectByNameSubject(boolean status, String key, Pageable pageable);

    boolean existsById(Long id);

    boolean existsByIdAndStatus(Long id, boolean status);

    boolean existsByNameSubject(String subjectName);

}
