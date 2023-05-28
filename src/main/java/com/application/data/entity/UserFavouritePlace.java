package com.application.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "favourite_place")
public class UserFavouritePlace extends AbstractEntity {

    private Long placeId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "place_info", columnDefinition = "text")
    private String placeInfo;
}
