package com.example.managerstudentpoint.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Table(name = "class")
public class GroupClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_name")
    private String className;

    @Column(name = "status")
    private Boolean status;

    @OneToMany(
            mappedBy = "groupClass"
//            , cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<User> userList;

    public GroupClass(String className) {
        this.className = className;
    }

    public GroupClass() {
    }
}
