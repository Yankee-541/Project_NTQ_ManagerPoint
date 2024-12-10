package com.example.managerstudentpoint.repository;

import com.example.managerstudentpoint.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByIdAndIsDelete(Long id, boolean isDelete);

    @Query("UPDATE User set isDelete = true where id = :id")
    void deleteById(Long id);

    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIsDeleteAndRollNumber(String username, boolean isDelete, String rollNumber);

    boolean existsByIdAndIsDelete(Long id, boolean delete);

    boolean existsByPhoneNumber(String phoneNum);

    boolean existsByEmail(String email);

    @Query(value = "select u from User u left join u.groupClass c " +
            "where (concat(u.fullName, u.rollNumber, u.username ) like %:key% " +
            "or c.className like %:key%) and (:status = u.isDelete) ")
    Page<User> getUsersAllByFullNameAndRollNumberAndUsername(boolean status,String key, Pageable pageable);

    User getUserByUsername(String name);

    User getUserByEmail(String email);

    @Query(value = "select rollnumber from mangerstudentpoint.user " +
            "order by rollnumber desc LIMIT 1;", nativeQuery = true)
    String getLastRollNumber();

}
