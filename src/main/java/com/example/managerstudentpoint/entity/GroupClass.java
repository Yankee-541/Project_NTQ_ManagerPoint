package com.example.managerstudentpoint.entity;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "class")
public class GroupClass extends BaseEntity {
    @Column(name = "class_name")
    private String className;

    @OneToMany(
            mappedBy = "groupClass"
    )
    private List<User>  userList = new ArrayList<>();


}
