package com.application.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyUnitDTO {
    private String time;
    private String temperature_2m_max;
    private String temperature_2m_min;
    private String rain_sum;
    private String windspeed_10m_max;
}
