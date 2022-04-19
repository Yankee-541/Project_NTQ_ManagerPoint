package com.example.managerstudentpoint.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User extends BaseEntity {
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
    private String status;

    @Column
    private String email;

    @Column(name = "phonenumber")
    private String phoneNumber;

    @OneToMany(
            mappedBy = "users", cascade = CascadeType.ALL
    )
    @JsonIgnore
    @Autowired
    private List<Reports> reports = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = true)
    @Autowired
    private GroupClass groupClass;



    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roleList = new HashSet<>();

}
