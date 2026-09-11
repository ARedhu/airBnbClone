package com.Ashish.airBnbClone.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingInitRequest {
    private Long hotelId;
    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer roomsCount;
}
