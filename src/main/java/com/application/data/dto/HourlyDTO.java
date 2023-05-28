package com.application.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HourlyDTO {
    private ArrayList<String> time;
    private ArrayList<Double> temperature_2m;
    private ArrayList<Double> rain;
    //TODO Deletable
    private ArrayList<Integer> relativehumidity_2m;
    private ArrayList<Double> windspeed_10m;
}
