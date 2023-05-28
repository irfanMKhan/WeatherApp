package com.application.data.service.implementation;


import com.application.data.dto.DailyForecastDTO;
import com.application.data.dto.HourlyForecastDTO;
import com.application.data.payload.response.WeatherForecastResponse;
import com.application.data.service.WeatherForecastService;
import com.application.utility.openAPI.MeteoWeather;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class WeatherForecastServiceImplementation implements WeatherForecastService {

    public WeatherForecastServiceImplementation() {
    }

    public WeatherForecastResponse getWeatherForecastForLocation(String latitude, String longitude, String tz) {
        try {
            String url = MeteoWeather.FORECAST_API_URL.formatted(latitude, longitude, tz);

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<WeatherForecastResponse> response = restTemplate.getForEntity(url, WeatherForecastResponse.class);
            ResponseEntity<Object> response1 = restTemplate.getForEntity(url, Object.class);

            WeatherForecastResponse weatherForecastResponse = response.getBody();

            return weatherForecastResponse;

        } catch (Throwable throwable) {
            Notification.show("Network Error", 5000, Notification.Position.TOP_CENTER);
            return null;
        }
    }

    public List<DailyForecastDTO> getDailyForeCast(WeatherForecastResponse weatherForecastResponse) {
        ArrayList<String> time = weatherForecastResponse.getDaily().getTime();
        ArrayList<Double> temperature_2m_min = weatherForecastResponse.getDaily().getTemperature_2m_min();
        ArrayList<Double> temperature_2m_max = weatherForecastResponse.getDaily().getTemperature_2m_max();
        ArrayList<Double> rain_sum = weatherForecastResponse.getDaily().getRain_sum();
        ArrayList<Double> windspeed_10m_max = weatherForecastResponse.getDaily().getWindspeed_10m_max();

        List<DailyForecastDTO> dailyForecastDTOList = new ArrayList<>();

        for (int i = 0; i < time.size(); i++) {
            dailyForecastDTOList.add(new DailyForecastDTO(
                            time.get(i),
                            temperature_2m_min.get(i),
                            temperature_2m_max.get(i),
                            rain_sum.get(i),
                            windspeed_10m_max.get(i)
                    )
            );
        }

        return dailyForecastDTOList;
    }

    @Override
    public List<HourlyForecastDTO> getHourlyForeCast(WeatherForecastResponse weatherForecastResponse, String date) {
        ArrayList<String> hourlyTimes = weatherForecastResponse.getHourly().getTime();
        ArrayList<Double> temperature_2m = weatherForecastResponse.getHourly().getTemperature_2m();
        ArrayList<Double> windspeed_10m = weatherForecastResponse.getHourly().getWindspeed_10m();
        ArrayList<Double> rain = weatherForecastResponse.getHourly().getRain();

        List<Integer> timeIndices = IntStream.range(0, hourlyTimes.size())
                .filter(i -> hourlyTimes.get(i).startsWith(date))
                .mapToObj(i -> i)
                .collect(Collectors.toList());

        List<HourlyForecastDTO> hourlyForecasts = new ArrayList<>();
        for (Integer idx : timeIndices) {
            hourlyForecasts.add(
                    new HourlyForecastDTO(
                            hourlyTimes.get(idx),
                            temperature_2m.get(idx),
                            rain.get(idx),
                            windspeed_10m.get(idx)
                    )
            );
        }

        return hourlyForecasts;
    }

}
