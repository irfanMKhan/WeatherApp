package com.application.data.payload.response;


import com.application.data.dto.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherForecastResponse {
    private double latitude;
    private double longitude;
    private double generationtime_ms;
    private int utc_offset_seconds;
    private String timezone;
    private String timezone_abbreviation;
    private double elevation;
    private CurrentWeatherDTO current_weather;
    private HourlyUnitDTO hourly_units;
    private HourlyDTO hourly;
    private DailyUnitDTO daily_units;
    private DailyDTO daily;

}


