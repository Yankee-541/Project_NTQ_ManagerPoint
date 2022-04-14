package com.example.managerstudentpoint.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "role")
public class Role extends BaseEntity {

    @Column(name = "name")
    private String roleName;

    @ManyToMany(mappedBy = "roleList")
    private List<User> users  = new ArrayList<>();


}
