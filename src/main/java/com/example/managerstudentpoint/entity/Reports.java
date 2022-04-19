package com.example.managerstudentpoint.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "report")
public class Reports extends BaseEntity {
    @Column(name = "point")
    private Double point;

//    @ManyToOne
//    @JoinColumn(name = "subject_id")
//    @Autowired
//    private Subject subject;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.DETACH})
    @JoinColumn(name = "user_id", nullable=false)
    private User users;
}
