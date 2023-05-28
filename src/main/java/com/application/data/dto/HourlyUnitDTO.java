package com.application.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HourlyUnitDTO {
    private String time;
    private String temperature_2m;
    private String rain;
    //TODO Deletable
    private String relativehumidity_2m;
    private String windspeed_10m;
}
