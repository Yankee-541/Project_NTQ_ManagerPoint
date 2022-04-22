package com.example.managerstudentpoint.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "subject")
public class Subject{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_subject")
    private String nameSubject;

//    @OneToMany(
//                mappedBy = "subject", cascade =  CascadeType.ALL, fetch = FetchType.EAGER
//    )
//    @Autowired
//    private List<Reports> reports = new ArrayList<>();


}
