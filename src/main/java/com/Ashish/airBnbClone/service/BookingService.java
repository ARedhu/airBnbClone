package com.Ashish.airBnbClone.service;

import com.Ashish.airBnbClone.dto.BookingDto;
import com.Ashish.airBnbClone.dto.BookingInitRequest;
import com.Ashish.airBnbClone.dto.GuestDto;

import java.util.List;

public interface BookingService {

    BookingDto initialiseBooking(BookingInitRequest bookingInitRequest);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);

    String initiatePayments(Long bookingId);
}
