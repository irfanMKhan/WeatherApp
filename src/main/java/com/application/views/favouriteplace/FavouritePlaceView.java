package com.application.views.favouriteplace;


import com.application.data.dto.CityGeoCodingDTO;
import com.application.data.service.UserFavouritePlaceService;
import com.application.security.Authentication;
import com.application.views.MainLayout;
import com.application.views.weatherdetail.WeatherDetail;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private HorizontalLayout createCard(CityGeoCodingDTO cityGeoCodingDTO) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        VerticalLayout description = new VerticalLayout();
        description.addClassName("location-item");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        Span name = new Span(cityGeoCodingDTO.getName());
        name.addClassName("name");
        Span date = new Span(cityGeoCodingDTO.getCountry());
        date.addClassName("date");
        header.add(name, date);

        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");

        Icon likeIcon = VaadinIcon.HEART.create();
        likeIcon.addClassName("icon");
        likeIcon.addClassName("icon-red");

        likeIcon.addClickListener(iconClickEvent -> {
            if (this.authentication.isLoggedIn()) {
                String msg = this.userFavouritePlaceService.makePlaceFavourite(cityGeoCodingDTO);
                Notification.show(msg);
                this.getCityList();
            } else {
                likeIcon.getUI().ifPresent(ui -> {
                    ui.navigate("login");
                });
            }
        });

        header.addClickListener(event -> {
            header.getUI().ifPresent(ui -> {
                QueryParameters params = QueryParameters.simple(Map.of(
                        "city", cityGeoCodingDTO.getName(),
                        "lon", String.valueOf(cityGeoCodingDTO.getLongitude()),
                        "lat", String.valueOf(cityGeoCodingDTO.getLatitude()),
                        "tz", String.valueOf(cityGeoCodingDTO.getTimezone())
                ));
                ui.navigate(WeatherDetail.class, params);
            });
        });

        actions.add(likeIcon);

        description.add(header, actions);
        card.add(description);
        return card;
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
        for (CityGeoCodingDTO cityGeoCodingDTO : cityGeoCodingDTOList) {
            cityListDiv.add(createCard(cityGeoCodingDTO));
        }
        if (cityGeoCodingDTOList.size() < 1) {
            add(notFoundFavouriteH2);
        }
    }
}
