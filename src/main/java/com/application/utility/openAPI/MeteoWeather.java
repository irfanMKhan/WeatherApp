package com.application.utility.openAPI;

public class MeteoWeather {

    private static final String BASE_URL = "https://";
    private static final String PREFIX_URL = "geocoding-";
    private static final String POSTFIX_URL = "api.open-meteo.com/v1";

    public static final String GEOCODING_API_URL = BASE_URL + PREFIX_URL + POSTFIX_URL + "/search?name=%s&count=100&language=en&format=json";
    public static final String FORECAST_API_URL = BASE_URL + POSTFIX_URL + "/forecast?" +
            "latitude=%s" +
            "&longitude=%s" +
            "&hourly=temperature_2m,rain,windspeed_10m&daily=temperature_2m_max,temperature_2m_min,rain_sum,windspeed_10m_max&current_weather=true" +
            "&timezone=%s";


}
