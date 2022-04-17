package com.example.managerstudentpoint.repository;

import com.example.managerstudentpoint.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByIdLike(String name);

//    @Query(value =
//            "select * from user u " +
//                    "where (:rollNumber = '' or : rollNumber = u.rollnumber) " +
//                    " and (:fullName = '' or :fullName = u.fullname) " +
//                    " and (:gender = '' or :gender = u.gender) " +
//                    " and (:address = '' or :address = u.address) " +
//                    " and (:status = '' or :status = u.status) " +
//                    " and (:email = '' or :email = u.email) " +
//                    " and (:phoneNumber ='' or phoneNumber = u.phonenumber) " +
//                    " limit :limit offset :offset"
//            , nativeQuery = true)
//
//
//    Page<User> getUserByCondition(
//            String fullName,
//            String rollNumber,
//            String gender,
//            String address,
//            String status,
//            String email,
//            String phoneNumber,
//            Integer limit,
//            Integer offset);


    @Query("select u from User u " +
            "where (:rollNumber = '' or : rollNumber = u.rollNumber) " +
                    " and (:fullName = '' or :fullName = u.fullName) " +
                    " and (:gender = '' or :gender = u.gender) " +
                    " and (:address = '' or :address = u.address) " +
                    " and (:status = '' or :status = u.status) " +
                    " and (:email = '' or :email = u.email) " +
                    " and (:phoneNumber ='' or :phoneNumber = u.phoneNumber) ")

    Page<User> getUserByCondition(
            String fullName,
            String rollNumber,
            String gender,
            String address,
            String status,
            String email,
            String phoneNumber,
            Pageable pageable);
}
