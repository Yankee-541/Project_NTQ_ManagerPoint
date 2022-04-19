package com.example.managerstudentpoint.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Data
@Table(name = "class")
public class GroupClass extends BaseEntity {
    @Column(name = "class_name")
    private String className;

    @OneToMany(
            mappedBy = "groupClass",cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<User> userList;
}
