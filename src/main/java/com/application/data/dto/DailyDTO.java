package com.application.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyDTO {
    private ArrayList<String> time;
    private ArrayList<Double> temperature_2m_max;
    private ArrayList<Double> temperature_2m_min;
    private ArrayList<Double> rain_sum;
    private ArrayList<Double> windspeed_10m_max;
}
