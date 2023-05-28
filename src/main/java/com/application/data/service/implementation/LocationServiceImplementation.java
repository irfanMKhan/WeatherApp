package com.application.data.service.implementation;

import com.application.data.dto.CityGeoCodingDTO;
import com.application.data.payload.response.GeoCodingResponse;
import com.application.data.service.LocationService;
import com.application.utility.openAPI.MeteoWeather;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationServiceImplementation implements LocationService {
    private List<CityGeoCodingDTO> cityGeoCodingDTOList;
    private Integer cityGeoCodingListCount;

    public LocationServiceImplementation() {
        this.cityGeoCodingDTOList = new ArrayList<>();
        this.cityGeoCodingListCount = 0;
    }

    public void getLocationData(String searchCity) {
        try {
            String url = MeteoWeather.GEOCODING_API_URL.formatted(searchCity);

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<GeoCodingResponse> response = restTemplate.getForEntity(url, GeoCodingResponse.class);

            GeoCodingResponse geoCodingResponse = response.getBody();

            if (geoCodingResponse == null || geoCodingResponse.getResults() == null)
                this.cityGeoCodingDTOList.clear();
            else
                this.cityGeoCodingDTOList = geoCodingResponse.getResults();

            this.cityGeoCodingListCount = this.cityGeoCodingDTOList.size();

        } catch (Throwable throwable) {
            Notification.show("Network Error", 5000, Notification.Position.TOP_CENTER);
            this.cityGeoCodingDTOList.clear();
            this.cityGeoCodingListCount = 0;
        }
    }

    public List<CityGeoCodingDTO> getLocationByCityNamePage(Integer page) {
        int offset = (page - 1) * 10;
        int limit = page * 10;

        if (limit >= this.cityGeoCodingDTOList.size()) {
            limit = this.cityGeoCodingDTOList.size() - 1;
        }

        return this.cityGeoCodingListCount == 0 ? this.cityGeoCodingDTOList : this.cityGeoCodingDTOList.subList(offset, limit);
    }

    public Integer getTotal() {
        return this.cityGeoCodingListCount;
    }
}
