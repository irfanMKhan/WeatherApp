package com.application.data.service;

import com.application.data.dto.DailyForecastDTO;
import com.application.data.dto.HourlyForecastDTO;
import com.application.data.payload.response.WeatherForecastResponse;

import java.util.List;

public interface WeatherForecastService {

    WeatherForecastResponse getWeatherForecastForLocation(String latitude, String longitude, String tz);

    List<DailyForecastDTO> getDailyForeCast(WeatherForecastResponse forecastResponse);

    List<HourlyForecastDTO> getHourlyForeCast(WeatherForecastResponse forecastResponse, String date);
}