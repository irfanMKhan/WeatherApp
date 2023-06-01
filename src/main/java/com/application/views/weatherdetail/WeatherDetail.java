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
    private final Div divCurrentWeather = new Div();
    private final Div divDailyForecast = new Div();
    private final Div divHourlyForecast = new Div();
    private WeatherForecastResponse weatherForecastResponse;

    WeatherDetail(WeatherForecastService weatherForecastService) {

        this.weatherForecastService = weatherForecastService;

        addClassName("card-list-view");
        setSizeFull();
        setMargin(true);

        H3 currentWeatherTitle = new H3("Today's Weather Forecast for: ");
        add(currentWeatherTitle);

        add(divCurrentWeather);

        VerticalLayout verticalLayoutDaily = new VerticalLayout();
        verticalLayoutDaily.setSpacing(true);
        verticalLayoutDaily.setPadding(true);
        verticalLayoutDaily.getThemeList().add("spacing");

        verticalLayoutDaily.add(new H3("7 Days Forecast"));
        divDailyForecast.getElement().setAttribute("style", "width: 100%");

        verticalLayoutDaily.add(divDailyForecast);


        VerticalLayout verticalLayoutHourly = new VerticalLayout();
        verticalLayoutHourly.setSpacing(false);
        verticalLayoutHourly.setPadding(false);
        verticalLayoutHourly.getThemeList().add("spacing-s");
        verticalLayoutHourly.add(hourlyForecastTitle);

        divHourlyForecast.getElement().setAttribute("style", "width: 100%");
        verticalLayoutHourly.add(divHourlyForecast);

        Div hourlyForecastContainer = new Div(verticalLayoutHourly);
        hourlyForecastContainer.getElement().setAttribute("style", "flex: 1");

        Div divTitleDailyForecast = new Div(verticalLayoutDaily);
        divTitleDailyForecast.getElement().setAttribute("style", "flex: 1");

        VerticalLayout horizontalLayoutForecast = new VerticalLayout();
        horizontalLayoutForecast.add(divTitleDailyForecast);

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
                divCurrentWeather.add(this.horizontalLayoutCurrentWeather(weatherForecastResponse));
                addDailyForecasts();
            }
        }
    }

    private void addDailyForecasts() {
        List<DailyForecastDTO> dailyForecastDTOList = this.weatherForecastService.getDailyForeCast(this.weatherForecastResponse);

        divDailyForecast.removeAll();
        divDailyForecast.addClassName("flex");

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

    private VerticalLayout dailyForecastsCard(String date, Double tempMin, Double tempMax, Double rain, Double windSpeed) {

        //date
        HorizontalLayout dateLayout = new HorizontalLayout();
        dateLayout.setSpacing(false);
        dateLayout.getThemeList().add("spacing-s");
        dateLayout.add(new Span("Date: "), new Span(date));
        dateLayout.addClassName("flex");
        dateLayout.addClassName("text-inline");

        //min temp
        HorizontalLayout minTempLayout = new HorizontalLayout();
        minTempLayout.setSpacing(false);
        minTempLayout.getThemeList().add("spacing-s");
        minTempLayout.add(new Span("Min Temp: "), new Span(tempMin + "°C"));
        minTempLayout.addClassName("flex");
        minTempLayout.addClassName("text-inline");

        //max temp
        HorizontalLayout maxTempLayout = new HorizontalLayout();
        maxTempLayout.setSpacing(false);
        maxTempLayout.getThemeList().add("spacing-s");
        maxTempLayout.add(new Span("Max Temp: "), new Span(tempMax + "°C"));
        maxTempLayout.addClassName("flex");
        maxTempLayout.addClassName("text-inline");

        //rain
        HorizontalLayout rainLayout = new HorizontalLayout();
        rainLayout.setSpacing(false);
        rainLayout.getThemeList().add("spacing-s");
        rainLayout.add(new Span("Rain Sum: "), new Span(rain + "mm"));
        rainLayout.addClassName("flex");
        rainLayout.addClassName("text-inline");

        //maxWindSpeed
        HorizontalLayout maxWindSpeed = new HorizontalLayout();
        maxWindSpeed.setSpacing(false);
        maxWindSpeed.getThemeList().add("spacing-s");
        maxWindSpeed.addClassName("flex");
        maxWindSpeed.addClassName("text-inline");

        maxWindSpeed.add(new Span("Max Wind Speed:"), new Span(windSpeed + "km/h"));

        VerticalLayout verticalLayoutCard = new VerticalLayout();
        verticalLayoutCard.addClassNames("card", "daily-forcast-item");
        verticalLayoutCard.setSpacing(false);
        verticalLayoutCard.getThemeList().add("spacing-s");

        verticalLayoutCard.addClickListener(event -> this.showHourlyForecasts(date));

        VerticalLayout verticalLayoutDescription = new VerticalLayout();
        verticalLayoutDescription.addClassName("description");
        verticalLayoutDescription.setSpacing(false);
        verticalLayoutDescription.setPadding(false);

        verticalLayoutDescription.add(
                dateLayout,
                minTempLayout,
                maxTempLayout,
                rainLayout,
                maxWindSpeed);
        verticalLayoutCard.add(verticalLayoutDescription);

        return verticalLayoutCard;
    }

    private void showHourlyForecasts(String date) {
        List<HourlyForecastDTO> hourlyForecastDTOList = this.weatherForecastService.getHourlyForeCast(this.weatherForecastResponse, date);

        divHourlyForecast.removeAll();
        divHourlyForecast.addClassName("grid");
        divHourlyForecast.addClassName("fullscreen");
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

    private VerticalLayout hourlyForecastsCard(String time, Double temp, Double rain, Double windSpeed) {


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

        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);
        description.add(
                dateLayout,
                minTempLayout,
                rainLayout,
                maxWindSpeed);

        VerticalLayout verticalLayoutHourlyCard = new VerticalLayout();
        verticalLayoutHourlyCard.addClassNames("card", "daily-forcast-item");
        verticalLayoutHourlyCard.setSpacing(false);
        verticalLayoutHourlyCard.getThemeList().add("spacing-s");
        verticalLayoutHourlyCard.add(description);
        return verticalLayoutHourlyCard;
    }


    private HorizontalLayout horizontalLayoutCurrentWeather(WeatherForecastResponse weatherForecastResponse) {

        HorizontalLayout horizontalLayoutTemperature = new HorizontalLayout();
        horizontalLayoutTemperature.addClassName("header");
        horizontalLayoutTemperature.setSpacing(true);
        horizontalLayoutTemperature.getThemeList().add("spacing");

        Span spanTemperature = new Span("Temperature: ");

        Span spanCurrentTemperature = new Span(weatherForecastResponse.getCurrent_weather().getTemperature() + " °C");

        horizontalLayoutTemperature.add(spanTemperature, spanCurrentTemperature);

        Span spanWindSpeed = new Span("Wind Speed: " + weatherForecastResponse.getCurrent_weather().getWindspeed() + " km/h");

        Span spanDayNight = new Span(weatherForecastResponse.getCurrent_weather().getIs_day() == 1 ? "Day" : "Night");

        String windDirection = getWindDirection(weatherForecastResponse.getCurrent_weather().getWinddirection());

        Span spanWindDirection = new Span("Wind Direction: " + windDirection);

        HorizontalLayout horizontalLayoutMainTitle = new HorizontalLayout();
        horizontalLayoutMainTitle.addClassName("description");
        horizontalLayoutMainTitle.setSpacing(true);
        horizontalLayoutMainTitle.add(spanDayNight, horizontalLayoutTemperature, spanWindSpeed, spanWindDirection);

        VerticalLayout verticalLayoutTopLayout = new VerticalLayout();
        verticalLayoutTopLayout.addClassName("card");
        verticalLayoutTopLayout.setSpacing(true);
        verticalLayoutTopLayout.getThemeList().add("spacing");
        verticalLayoutTopLayout.add(mainTitle, horizontalLayoutMainTitle);

        HorizontalLayout horizontalLayoutCurrentWeatherCard = new HorizontalLayout();
        horizontalLayoutCurrentWeatherCard.add(verticalLayoutTopLayout);

        return horizontalLayoutCurrentWeatherCard;
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
