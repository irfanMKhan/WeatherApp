package com.application.data.service;

import com.application.data.dto.CityGeoCodingDTO;
import com.application.data.entity.UserFavouritePlace;

import java.util.List;

public interface UserFavouritePlaceService {
    List<UserFavouritePlace> getCurrentUserFavouritePlaces();

    List<CityGeoCodingDTO> getCurrentUserFavouriteCityGeoCoding();

    String makePlaceFavourite(CityGeoCodingDTO cityGeoCodingDTO);
}
