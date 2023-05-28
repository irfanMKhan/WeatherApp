package com.application.views.Home;

import com.application.data.dto.CityGeoCodingDTO;
import com.application.data.service.LocationService;
import com.application.data.service.UserFavouritePlaceService;
import com.application.security.Authentication;
import com.application.views.MainLayout;
import com.application.views.weatherdetail.WeatherDetail;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@PermitAll
@PageTitle("Weather App")
@Route(value = "home", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class HomeView extends VerticalLayout implements AfterNavigationObserver {

    private final LocationService locationService;
    private final Authentication authentication;
    private final UserFavouritePlaceService userFavouritePlaceService;

    /* html elements */

    private final TextField textFieldCityName = new TextField("City Name", "Find by City Name");
    private final Button buttonSearch = new Button("Search");

    private final Div divCityList = new Div();
    private final Div divPagination = new Div();

    private final Select<Integer> selectPage = new Select<>();
    private final Button buttonNext = new Button("Next");
    private final Button buttonPrevious = new Button("Previous");
    private List<Integer> listPerPage = new ArrayList<>();
    private Integer currentPage = 1;

    public HomeView(LocationService locationService, Authentication authentication, UserFavouritePlaceService userFavouritePlaceService) {
        this.locationService = locationService;
        this.authentication = authentication;
        this.userFavouritePlaceService = userFavouritePlaceService;

        buttonSearch.addClickListener(e -> {
            String searchCityName = textFieldCityName.getValue().trim();
            if (!searchCityName.equals("")) {
                VaadinSession.getCurrent().setAttribute("searched_city", searchCityName);
                this.getCityList();
            }
        });
        buttonSearch.addClickShortcut(Key.ENTER);

        setMargin(true);
        HorizontalLayout firstLayout = new HorizontalLayout(textFieldCityName, buttonSearch);
        firstLayout.setAlignItems(Alignment.END);
        firstLayout.add(textFieldCityName, buttonSearch);
        add(firstLayout);

        addClassName("card-list-view");
        divCityList.getElement().setAttribute("style", "min-height: 8rem; overflow-y:scroll");
        add(divCityList);

        HorizontalLayout thirdLayout = new HorizontalLayout(buttonPrevious, selectPage, buttonNext);
        thirdLayout.setAlignItems(Alignment.END);
        divPagination.add(thirdLayout);
        this.addPaginationSelectListener();
        this.addPaginationButtonClickListener();
    }

    private void getCityList() {
        String searchCity = this.textFieldCityName.getValue().trim();
        this.locationService.getLocationData(searchCity);

        if (this.locationService.getTotal() > 0) {
            add(divPagination);
            createPagination(this.locationService.getTotal());
        } else {
            this.addCitListDiv(this.locationService.getLocationByCityNamePage(1));
            divCityList.add(new H3("Result Not Found"));
        }
    }

    private void addCitListDiv(List<CityGeoCodingDTO> cityGeoCodingDTOList) {
        divCityList.removeAll();
        for (CityGeoCodingDTO cityGeoCoding : cityGeoCodingDTOList) {
            divCityList.add(createCard(cityGeoCoding));
        }
    }

    private void createPagination(Integer total) {
        int perPage = 10;
        int totalPage = (int) Math.ceil(((double) total / perPage));
        this.listPerPage = IntStream.range(1, totalPage + 1).boxed().collect(Collectors.toList());

        selectPage.setLabel("Pages");
        selectPage.setItems(this.listPerPage);
        selectPage.setValue(this.currentPage);
    }

    private HorizontalLayout createCard(CityGeoCodingDTO cityGeoCodingDTO) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        VerticalLayout description = new VerticalLayout();
        description.addClassName("location-item");
        description.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.addClassName("headerPointer");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        Span name = new Span(cityGeoCodingDTO.getName());
        name.addClassName("name");
        Span spanCountry = new Span(cityGeoCodingDTO.getCountry());
        spanCountry.addClassName("date");
        header.add(name, spanCountry);

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
                Notification.show(msg, 5000, Notification.Position.TOP_CENTER);
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

    private void addPaginationSelectListener() {
        this.selectPage.addValueChangeListener(e -> {
            Integer page = e.getValue();
            if (page != null) {
                this.currentPage = page;
                this.addCitListDiv(this.locationService.getLocationByCityNamePage(page));
            }
        });
    }

    private void addPaginationButtonClickListener() {
        this.buttonNext.addClickListener(e -> {
            if (this.canNavigate(this.currentPage + 1)) {
                selectPage.setValue(this.currentPage + 1);
            }
        });

        this.buttonPrevious.addClickListener(e -> {
            if (this.canNavigate(this.currentPage - 1)) {
                selectPage.setValue(this.currentPage - 1);
            }
        });
    }

    private Boolean canNavigate(int page) {
        if (this.listPerPage.size() > 0) {
            Integer min = this.listPerPage.get(0);
            Integer max = this.listPerPage.get(listPerPage.size() - 1);

            return page >= min && page <= max;
        }
        return false;
    }

    /* below method is overridden for AfterNavigationObserver implementation */
    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        String searchCity = this.textFieldCityName.getValue();
        if (searchCity.equals("")) {
            searchCity = (String) VaadinSession.getCurrent().getAttribute("searched_city");
        }
        if (searchCity != null) this.textFieldCityName.setValue(searchCity);

        this.getCityList();
    }

}
