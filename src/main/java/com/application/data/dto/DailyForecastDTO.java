package com.application.data.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyForecastDTO {
    private String date;
    private Double minTemperature;
    private Double maxTemperature;
    private Double rainSum;
    private Double maxWindSpeed;
}
