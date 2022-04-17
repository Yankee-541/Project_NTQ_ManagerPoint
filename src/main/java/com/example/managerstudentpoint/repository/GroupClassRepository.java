package com.example.managerstudentpoint.repository;

import com.example.managerstudentpoint.entity.GroupClass;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupClassRepository extends JpaRepository<GroupClass, Long> {
//    Page<GroupClass> findAllByIdAndClassNameLike (Integer id, String name);
}
