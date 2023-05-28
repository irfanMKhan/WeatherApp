package com.application.data.service.implementation;


import com.application.data.dto.CityGeoCodingDTO;
import com.application.data.entity.User;
import com.application.data.entity.UserFavouritePlace;
import com.application.data.repository.UserFavouritePlaceRepository;
import com.application.data.service.UserFavouritePlaceService;
import com.application.security.Authentication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserFavouritePlaceServiceImplementation implements UserFavouritePlaceService {

    private final UserFavouritePlaceRepository userFavouritePlaceRepository;
    private final Authentication authentication;
    private final UserServiceImplementation userServiceImplementation;


    public UserFavouritePlaceServiceImplementation(
            Authentication authentication,
            UserServiceImplementation userServiceImplementation,
            UserFavouritePlaceRepository userFavouritePlaceRepository
    ) {
        this.authentication = authentication;
        this.userServiceImplementation = userServiceImplementation;
        this.userFavouritePlaceRepository = userFavouritePlaceRepository;
    }

    @Override
    public List<UserFavouritePlace> getCurrentUserFavouritePlaces() {
        User user = this.getCurrentAppUser();
        if (user != null) {
            return this.userFavouritePlaceRepository.findAllByUserId(user.getId());
        }
        return null;
    }

    @Override
    public List<CityGeoCodingDTO> getCurrentUserFavouriteCityGeoCoding() {
        List<UserFavouritePlace> currentUserFavouritePlaces = this.getCurrentUserFavouritePlaces();
        List<CityGeoCodingDTO> cityGeoCodingDTOList = null;
        if (currentUserFavouritePlaces != null) {
            cityGeoCodingDTOList = new ArrayList<>();
            for (UserFavouritePlace userFavouritePlace : currentUserFavouritePlaces) {
                try {
                    CityGeoCodingDTO cityGeoCodingDTO = new ObjectMapper().readValue(userFavouritePlace.getPlaceInfo(), CityGeoCodingDTO.class);
                    cityGeoCodingDTOList.add(cityGeoCodingDTO);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return cityGeoCodingDTOList;
    }

    @Override
    public String makePlaceFavourite(CityGeoCodingDTO cityGeoCodingDTO) {
        User user = this.getCurrentAppUser();
        if (user != null) {
            try {
                UserFavouritePlace userFavouritePlace = this.userFavouritePlaceRepository.findByPlaceId((long) cityGeoCodingDTO.getId());

                if (userFavouritePlace == null) {
                    String palaceInfoString = new ObjectMapper().writeValueAsString(cityGeoCodingDTO);

                    userFavouritePlace = new UserFavouritePlace();
                    userFavouritePlace.setPlaceId((long) cityGeoCodingDTO.getId());
                    userFavouritePlace.setPlaceInfo(palaceInfoString);
                    userFavouritePlace.setUser(user);

                    this.userFavouritePlaceRepository.save(userFavouritePlace);
                    return cityGeoCodingDTO.getName() + " is add to favourite place";
                } else {
                    this.userFavouritePlaceRepository.delete(userFavouritePlace);
                    return cityGeoCodingDTO.getName() + " is remove from favourite place";
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return "Failed to Make Place Favourite";
    }

    private User getCurrentAppUser() {
        Optional<User> authenticatedUser = authentication.getAuthenticatedUser();
        User user = null;
        if (authenticatedUser.isPresent()) {
            user = this.userServiceImplementation.findUserByUserName(authenticatedUser.get().getUsername());
        }
        return user;
    }
}
