package com.example.managerstudentpoint.repository;

import com.example.managerstudentpoint.entity.GroupClass;
import com.example.managerstudentpoint.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupClassRepository extends JpaRepository<GroupClass, Long> {
    boolean existsByClassName(String username);

    GroupClass findByClassName(String name);

    @Query(value = "SELECT s FROM GroupClass s where (concat( s.className, s.id ) like %:key%) and (:status = s.status) ")
    List<GroupClass> getGroupClassByClassName(boolean status, String key);

    @Query(value = "select u from User u join u.groupClass g " +
            " where concat(g.id, g.className) like %:className% and g.status =:isDelete")
    List<User> findAllStudentByClassName(boolean isDelete, String className);

}
