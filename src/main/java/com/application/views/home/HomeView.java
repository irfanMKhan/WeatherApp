package com.application.views.home;

import com.application.data.dto.CityGeoCodingDTO;
import com.application.data.service.LocationService;
import com.application.data.service.UserFavouritePlaceService;
import com.application.security.Authentication;
import com.application.views.MainLayout;
import com.application.views.common.WeatherCard;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;
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

    private final TextField textFieldCityName = new TextField("City Name", "Find City Name");
    private final Button buttonSearch = new Button("Find");

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
        buttonSearch.addClassName("button-pointer");

        setMargin(true);
        HorizontalLayout firstLayout = new HorizontalLayout(textFieldCityName, buttonSearch);
        firstLayout.setAlignItems(Alignment.END);
        firstLayout.add(textFieldCityName, buttonSearch);
        add(firstLayout);

        addClassName("card-list-view");
        divCityList.getElement().setAttribute("style", "min-height: 8rem;");
        add(divCityList);

        buttonPrevious.addClassName("button-pointer");
        buttonNext.addClassName("button-pointer");

        HorizontalLayout thirdLayout = new HorizontalLayout(buttonPrevious, selectPage, buttonNext);
        thirdLayout.setAlignItems(Alignment.END);
        divPagination.add(thirdLayout);
        this.addPaginationSelectListener();
        this.addPaginationButtonClickListener();
    }

    private void getCityList() {
        String searchCity = this.textFieldCityName.getValue().trim();
        locationService.getLocationData(searchCity);

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
        WeatherCard weatherCard = new WeatherCard(authentication, userFavouritePlaceService);
        for (CityGeoCodingDTO cityGeoCoding : cityGeoCodingDTOList) {
            divCityList.add(weatherCard.createCardCityDetails(cityGeoCoding));
        }
    }

    private void createPagination(Integer total) {
        int perPage = 10;
        int totalPage = (int) Math.ceil(((double) total / perPage));
        this.listPerPage = IntStream.range(1, totalPage + 1).boxed().collect(Collectors.toList());

        selectPage.setLabel("Page");
        selectPage.setItems(this.listPerPage);
        selectPage.setValue(this.currentPage);
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
        this.getCityList();
    }

}
