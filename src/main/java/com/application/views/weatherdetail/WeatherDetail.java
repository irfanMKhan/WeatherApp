package com.application.views.weatherdetail;


import com.application.data.dto.DailyForecastDTO;
import com.application.data.dto.HourlyForecastDTO;
import com.application.data.payload.response.WeatherForecastResponse;
import com.application.data.service.WeatherForecastService;
import com.application.views.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Map;

@PermitAll
@PageTitle("Weather Forecasts")
@Route(value = "weather-detail/", layout = MainLayout.class)
public class WeatherDetail extends VerticalLayout implements BeforeEnterObserver, AfterNavigationObserver {

    private final WeatherForecastService weatherForecastService;
    private final H2 mainTitle = new H2();

    /* html elements */
    private final H3 hourlyForecastTitle = new H3("Hourly Forecasts");
    private final HorizontalLayout horizontalLayoutForecast = new HorizontalLayout();
    private final Div divDailyForecast = new Div();
    private final Div divHourlyForecast = new Div();
    private final Div divCurrentWeather = new Div();
    private WeatherForecastResponse weatherForecastResponse;

    WeatherDetail(WeatherForecastService weatherForecastService) {

        this.weatherForecastService = weatherForecastService;

        addClassName("card-list-view");
        setSizeFull();
        setMargin(true);

        add(divCurrentWeather);

        HorizontalLayout horizontalLayoutDaily = new HorizontalLayout();
        horizontalLayoutDaily.setSpacing(true);
        horizontalLayoutDaily.setPadding(true);
        horizontalLayoutDaily.getThemeList().add("spacing");

        horizontalLayoutDaily.add(new H3("7 Days Forecast"));
        divDailyForecast.getElement().setAttribute("style", "width: 100%");

        horizontalLayoutDaily.add(divDailyForecast);

        Div divTitleDailyForecast = new Div(horizontalLayoutDaily);
        divTitleDailyForecast.getElement().setAttribute("style", "flex: 1");

        horizontalLayoutForecast.add(divTitleDailyForecast);

        VerticalLayout hourlyForecastLayout = new VerticalLayout();
        hourlyForecastLayout.setSpacing(false);
        hourlyForecastLayout.setPadding(false);
        hourlyForecastLayout.getThemeList().add("spacing-s");

        hourlyForecastLayout.add(hourlyForecastTitle);
        divHourlyForecast.getElement().setAttribute("style", "width: 100%");
        hourlyForecastLayout.add(divHourlyForecast);

        Div hourlyForecastContainer = new Div(hourlyForecastLayout);
        hourlyForecastContainer.getElement().setAttribute("style", "flex: 1");

        horizontalLayoutForecast.add(hourlyForecastContainer);

        add(horizontalLayoutForecast);
    }

    private Map<String, List<String>> getCityName(Location location) {
        QueryParameters queryParameters = location.getQueryParameters();

        return queryParameters.getParameters();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Location location = event.getLocation();

        Map<String, List<String>> parametersMap = getCityName(location);

        String cityName = null;

        if (parametersMap.containsKey("city")) {
            cityName = parametersMap.get("city").get(0);
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        Location location = event.getLocation();

        Map<String, List<String>> parametersMap = getCityName(location);

        String cityName = null;

        if (parametersMap.containsKey("city")) {
            cityName = parametersMap.get("city").get(0);
        }
        String longitude = null;

        if (parametersMap.containsKey("lon")) {
            longitude = parametersMap.get("lon").get(0);
        }

        String latitude = null;

        if (parametersMap.containsKey("lat")) {
            latitude = parametersMap.get("lat").get(0);
        }

        String tz = null;

        if (parametersMap.containsKey("tz")) {
            tz = parametersMap.get("tz").get(0);
        }

        mainTitle.setText(cityName);

        if (latitude != null && longitude != null && tz != null) {
            this.weatherForecastResponse = this.weatherForecastService.getWeatherForecastForLocation(latitude, longitude, tz);
            if (this.weatherForecastResponse != null) {
                divCurrentWeather.add(this.createCard(weatherForecastResponse));
                addDailyForecasts();
            }
        }
    }

    private void addDailyForecasts() {
        List<DailyForecastDTO> dailyForecastDTOList = this.weatherForecastService.getDailyForeCast(this.weatherForecastResponse);

        divDailyForecast.removeAll();
        for (DailyForecastDTO dailyForecastDTO : dailyForecastDTOList) {
            divDailyForecast.add(dailyForecastsCard(
                            dailyForecastDTO.getDate(),
                            dailyForecastDTO.getMinTemperature(),
                            dailyForecastDTO.getMinTemperature(),
                            dailyForecastDTO.getRainSum(),
                            dailyForecastDTO.getMaxWindSpeed()
                    )
            );
        }
    }

    private HorizontalLayout dailyForecastsCard(String date, Double tempMin, Double tempMax, Double rain, Double windSpeed) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassNames("card", "daily-forcast-item");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        //date
        HorizontalLayout dateLayout = new HorizontalLayout();
        dateLayout.setSpacing(false);
        dateLayout.getThemeList().add("spacing-s");

        dateLayout.add(new Span("Date"), new Span(date));

        //min temp
        HorizontalLayout minTempLayout = new HorizontalLayout();
        minTempLayout.setSpacing(false);
        minTempLayout.getThemeList().add("spacing-s");

        minTempLayout.add(new Span("Minimum Temperature"), new Span(tempMin + "°C"));

        //max temp
        HorizontalLayout maxTempLayout = new HorizontalLayout();
        maxTempLayout.setSpacing(false);
        maxTempLayout.getThemeList().add("spacing-s");

        maxTempLayout.add(new Span("Maximum Temperature"), new Span(tempMax + "°C"));

        //rain
        HorizontalLayout rainLayout = new HorizontalLayout();
        rainLayout.setSpacing(false);
        rainLayout.getThemeList().add("spacing-s");

        rainLayout.add(new Span("Rain Sum"), new Span(rain + "mm"));

        //maxWindSpeed
        HorizontalLayout maxWindSpeed = new HorizontalLayout();
        maxWindSpeed.setSpacing(false);
        maxWindSpeed.getThemeList().add("spacing-s");

        maxWindSpeed.add(new Span("Maximum Wind Speed"), new Span(windSpeed + "km/h"));

        card.addClickListener(event -> this.showHourlyForecasts(date));

        description.add(
                dateLayout,
                minTempLayout,
                maxTempLayout,
                rainLayout,
                maxWindSpeed);
        card.add(description);
        return card;
    }

    private void showHourlyForecasts(String date) {
        List<HourlyForecastDTO> hourlyForecastDTOList = this.weatherForecastService.getHourlyForeCast(this.weatherForecastResponse, date);

        divHourlyForecast.removeAll();
        this.hourlyForecastTitle.setText("Hourly Forecasts of " + date);

        for (HourlyForecastDTO hourlyForecastDTO : hourlyForecastDTOList) {
            divHourlyForecast.add(
                    hourlyForecastsCard(
                            hourlyForecastDTO.getHour(),
                            hourlyForecastDTO.getTemperature(),
                            hourlyForecastDTO.getRain(),
                            hourlyForecastDTO.getWindSpeed()
                    )
            );
        }
    }

    private HorizontalLayout hourlyForecastsCard(String time, Double temp, Double rain, Double windSpeed) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassNames("card", "daily-forcast-item");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        //date
        HorizontalLayout dateLayout = new HorizontalLayout();
        dateLayout.setSpacing(false);
        dateLayout.getThemeList().add("spacing-s");

        dateLayout.add(new Span("Hour"), new Span(time.split("T")[1]));

        //temp
        HorizontalLayout minTempLayout = new HorizontalLayout();
        minTempLayout.setSpacing(false);
        minTempLayout.getThemeList().add("spacing-s");

        minTempLayout.add(new Span("Temperature"), new Span(temp + "°C"));

        //rain
        HorizontalLayout rainLayout = new HorizontalLayout();
        rainLayout.setSpacing(false);
        rainLayout.getThemeList().add("spacing-s");

        rainLayout.add(new Span("Rain"), new Span(rain + "mm"));

        //maxWindSpeed
        HorizontalLayout maxWindSpeed = new HorizontalLayout();
        maxWindSpeed.setSpacing(false);
        maxWindSpeed.getThemeList().add("spacing-s");

        maxWindSpeed.add(new Span("Wind Speed"), new Span(windSpeed + "km/h"));

        description.add(
                dateLayout,
                minTempLayout,
                rainLayout,
                maxWindSpeed);
        card.add(description);
        return card;
    }


    private HorizontalLayout createCard(WeatherForecastResponse weatherForecastResponse) {
        VerticalLayout verticalLayoutTopLayout = new VerticalLayout();
        verticalLayoutTopLayout.addClassName("card");
        verticalLayoutTopLayout.setSpacing(true);
        verticalLayoutTopLayout.getThemeList().add("spacing");

        HorizontalLayout card = new HorizontalLayout();

        HorizontalLayout horizontalLayoutMainTitle = new HorizontalLayout();
        horizontalLayoutMainTitle.addClassName("description");
        horizontalLayoutMainTitle.setSpacing(true);

        HorizontalLayout horizontalLayoutTemperature = new HorizontalLayout();
        horizontalLayoutTemperature.addClassName("header");
        horizontalLayoutTemperature.setSpacing(true);
        horizontalLayoutTemperature.getThemeList().add("spacing");

        Span spanTemperature = new Span("Temperature: ");
//        spanTemperature.addClassName("temperature");

        Span spanCurrentTemperature = new Span(weatherForecastResponse.getCurrent_weather().getTemperature() + " °C");
//        spanCurrentTemperature.addClassName("current-temperature");

        horizontalLayoutTemperature.add(spanTemperature, spanCurrentTemperature);

        Span spanWindSpeed = new Span("Wind Speed: " + weatherForecastResponse.getCurrent_weather().getWindspeed() + " km/h");
//        spanWindSpeed.addClassName("current-temperature");

        Span spanDayNight = new Span(weatherForecastResponse.getCurrent_weather().getIs_day() == 1 ? "Day" : "Night");
//        spanDayNight.addClassName("current-temperature");

        String windDirection = getWindDirection(weatherForecastResponse.getCurrent_weather().getWinddirection());

        Span spanWindDirection = new Span("Wind Direction: " + windDirection);
//        spanWindDirection.addClassName("current-temperature");

        horizontalLayoutMainTitle.add(spanDayNight,horizontalLayoutTemperature, spanWindSpeed,  spanWindDirection);

        verticalLayoutTopLayout.add(mainTitle, horizontalLayoutMainTitle);
        card.add(verticalLayoutTopLayout);

        return card;
    }

    private String getWindDirection(double angle) {
        String windDirection = "";

        if (30 < angle && angle <= 70)
            windDirection = "NE";
        if (70 < angle && angle <= 120)
            windDirection = "E";
        if (120 < angle && angle <= 160)
            windDirection = "SE";
        if (160 < angle && angle <= 210)
            windDirection = "S";
        if (210 < angle && angle <= 250)
            windDirection = "SW";
        if (250 < angle && angle <= 300)
            windDirection = "W";
        if (300 < angle && angle <= 340)
            windDirection = "NW";
        if (340 < angle || angle <= 30)
            windDirection = "N";

        return windDirection;
    }

}
