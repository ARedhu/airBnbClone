package com.Ashish.airBnbClone.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class HotelContactInfo {
    private String address;
    private String phoneNumber;
    private String email;
    private String location;
}
// @Embeddable tells JPA that this class doesn't have its own table. Its fields should be stored inside the table of another entity.