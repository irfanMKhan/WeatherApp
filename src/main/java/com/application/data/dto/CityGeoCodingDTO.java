package com.application.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityGeoCodingDTO {

    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private double elevation;
    private String timezone;
    private String feature_code;
    private String country_code;
    private int admin1_id;
    private int admin2_id;
    private int admin3_id;
    private int admin4_id;
    private int population;
    //TODO DELETABLE
    private ArrayList<String> postcodes;
    private int country_id;
    private String country;
    private String admin1;
    private String admin2;
    private String admin3;
    private String admin4;

}
