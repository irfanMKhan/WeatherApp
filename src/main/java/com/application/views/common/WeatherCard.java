package com.application.views.common;

import com.application.data.dto.CityGeoCodingDTO;
import com.application.data.service.UserFavouritePlaceService;
import com.application.security.Authentication;
import com.application.views.weatherdetail.WeatherDetail;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.QueryParameters;

import java.util.Map;

public class WeatherCard {
    private final Authentication authentication;
    private final UserFavouritePlaceService userFavouritePlaceService;

    public WeatherCard(Authentication authentication, UserFavouritePlaceService userFavouritePlaceService) {
        this.authentication = authentication;
        this.userFavouritePlaceService = userFavouritePlaceService;
    }

    public HorizontalLayout createCardCityDetails(CityGeoCodingDTO cityGeoCodingDTO) {

        Span spanCityName = new Span(cityGeoCodingDTO.getName());
        spanCityName.addClassName("name");
        Span spanCountry = new Span("Country: " + cityGeoCodingDTO.getCountry());
        spanCountry.addClassName("date");
        Span spanTimeZone = new Span("Timezone: " + cityGeoCodingDTO.getTimezone());
        spanTimeZone.addClassName("date");

        Icon iconHeart = VaadinIcon.HEART.create();
        iconHeart.addClassName("icon");
        iconHeart.addClassName("icon-red");
        iconHeart.addClassName("button-pointer");

        iconHeart.addClickListener(iconClickEvent -> {
            if (this.authentication.isLoggedIn()) {
                String msg = this.userFavouritePlaceService.makePlaceFavourite(cityGeoCodingDTO);
                Notification.show(msg, 5000, Notification.Position.TOP_CENTER);
            } else {
                iconHeart.getUI().ifPresent(ui -> {
                    ui.navigate("login");
                });
            }
        });

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.addClassName("headerPointer");
        header.add(spanCityName, spanCountry, spanTimeZone);

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

        Span spanFavorite = new Span("Add/Remove favorite location --> ");
        spanFavorite.addClassName("text-tertiary");

        HorizontalLayout horizontalLayoutWithAction = new HorizontalLayout();
        horizontalLayoutWithAction.addClassName("actions");
        horizontalLayoutWithAction.getThemeList().add("spacing-s");
        horizontalLayoutWithAction.setPadding(false);
        horizontalLayoutWithAction.setSpacing(false);
        horizontalLayoutWithAction.setMargin(false);
        horizontalLayoutWithAction.add(spanFavorite, iconHeart);

        VerticalLayout verticalLayoutCardWithIcon = new VerticalLayout();
        verticalLayoutCardWithIcon.addClassName("location-item");
        verticalLayoutCardWithIcon.setPadding(false);
        verticalLayoutCardWithIcon.setSpacing(false);
        verticalLayoutCardWithIcon.setMargin(false);
        verticalLayoutCardWithIcon.add(header, horizontalLayoutWithAction);

        HorizontalLayout horizontalLayoutCard = new HorizontalLayout();
        horizontalLayoutCard.addClassName("card");
        horizontalLayoutCard.getThemeList().add("spacing-s");
        horizontalLayoutCard.setSpacing(false);
        horizontalLayoutCard.add(verticalLayoutCardWithIcon);
        return horizontalLayoutCard;
    }
}
