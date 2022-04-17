package com.example.managerstudentpoint.dto;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class BaseAbstractDTO<T> {
    private Long id;

}
