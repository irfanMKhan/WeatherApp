package com.application.data.repository;


import com.application.data.entity.UserFavouritePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavouritePlaceRepository extends JpaRepository<UserFavouritePlace, Long>, JpaSpecificationExecutor<UserFavouritePlace> {

    List<UserFavouritePlace> findAllByUserId(Long userId);

    UserFavouritePlace findByPlaceId(Long placeId);

}
