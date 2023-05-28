package com.application.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HourlyForecastDTO {
    private String hour;
    private Double temperature;
    private Double rain;
    private Double windSpeed;
}
