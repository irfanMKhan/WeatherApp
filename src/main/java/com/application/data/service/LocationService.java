package com.application.data.service;

import com.application.data.dto.CityGeoCodingDTO;

import java.util.List;

public interface LocationService {
    void getLocationData(String searchCity);

    List<CityGeoCodingDTO> getLocationByCityNamePage(Integer page);

    Integer getTotal();
}
