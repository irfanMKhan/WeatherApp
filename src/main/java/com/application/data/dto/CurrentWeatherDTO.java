package com.application.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentWeatherDTO {
    private double temperature;
    private double windspeed;
    private double winddirection;
    private int weathercode;
    private int is_day;
    private String time;
}
