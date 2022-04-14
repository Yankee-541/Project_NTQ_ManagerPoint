package com.example.managerstudentpoint.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "user")
public class User extends BaseEntity{
    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String passWord;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address")
    private String address;

    @Column(name = "status")
    private String status;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private Long phoneNumber;


    @OneToMany(
            mappedBy = "users"
    )
    private List<Reports> reports = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "class_id")
    private GroupClass groupClass;

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roleList = new ArrayList<>();

}
