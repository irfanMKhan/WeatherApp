package com.application.data.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SampleAddress extends AbstractEntity {

    private String street;
    private String postalCode;
    private String city;
    private String state;
    private String country;

}
