package com.application.views.favouriteplace;


import com.application.data.dto.CityGeoCodingDTO;
import com.application.data.service.UserFavouritePlaceService;
import com.application.security.Authentication;
import com.application.views.MainLayout;
import com.application.views.common.WeatherCard;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;

@PermitAll
@PageTitle("Favourite Places")
@Route(value = "favourite-place", layout = MainLayout.class)
public class FavouritePlaceView extends VerticalLayout implements AfterNavigationObserver {

    private final Authentication authentication;
    private final UserFavouritePlaceService userFavouritePlaceService;

    /* html elements */

    private final Div cityListDiv = new Div();
    private final H2 notFoundFavouriteH2 = new H2("No Favourite Place Found");

    public FavouritePlaceView(
            Authentication authentication,
            UserFavouritePlaceService userFavouritePlaceService
    ) {
        this.authentication = authentication;
        this.userFavouritePlaceService = userFavouritePlaceService;

        setMargin(true);
        addClassName("card-list-view");
        add(cityListDiv);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        this.getCityList();
    }

    private void getCityList() {
        List<CityGeoCodingDTO> cityGeoCodingDTOList = this.userFavouritePlaceService.getCurrentUserFavouriteCityGeoCoding();
        if (cityGeoCodingDTOList == null) cityGeoCodingDTOList = new ArrayList<>();
        addCitListDiv(cityGeoCodingDTOList);
    }

    private void addCitListDiv(List<CityGeoCodingDTO> cityGeoCodingDTOList) {
        cityListDiv.removeAll();
        WeatherCard weatherCard = new WeatherCard(authentication, userFavouritePlaceService);
        for (CityGeoCodingDTO cityGeoCodingDTO : cityGeoCodingDTOList) {
            cityListDiv.add(weatherCard.createCardCityDetails(cityGeoCodingDTO));
        }
        if (cityGeoCodingDTOList.size() < 1) {
            add(notFoundFavouriteH2);
        }
    }
}
