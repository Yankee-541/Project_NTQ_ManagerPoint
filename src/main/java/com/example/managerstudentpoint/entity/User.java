package com.example.managerstudentpoint.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "rollnumber")
    private String rollNumber;

    @Column
    private String gender;

    @Column
    private String address;

    @Column
    private Boolean isDelete = Boolean.FALSE;

    @Column
    private String email;

    @Column(name = "phonenumber")
    private String phoneNumber;

    public User(String username,
                String fullName,
                String rollNumber,
                String gender,
                String address,
                String email,
                String phoneNumber) {
        this.username = username;
        this.fullName = fullName;
        this.rollNumber = rollNumber;
        this.gender = gender;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User(String username,
                String password,
                String fullName,
                String rollNumber,
                String gender,
                String address,
                Boolean isDelete,
                String email,
                String phoneNumber) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.rollNumber = rollNumber;
        this.gender = gender;
        this.address = address;
        this.isDelete = isDelete;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User(Long id,
                String password,
                String fullName,
                String rollNumber,
                String gender,
                String address,
                String email,
                String phoneNumber) {
        this.id = id;
        this.password = password;
        this.fullName = fullName;
        this.rollNumber = rollNumber;
        this.gender = gender;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    @OneToMany(
            mappedBy = "users"
//            , cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<Score> reports = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = true)
    private GroupClass groupClass;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roleList = new HashSet<>();

}
