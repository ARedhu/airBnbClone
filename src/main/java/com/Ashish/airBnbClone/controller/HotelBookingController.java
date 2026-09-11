package com.Ashish.airBnbClone.controller;

import com.Ashish.airBnbClone.dto.BookingDto;
import com.Ashish.airBnbClone.dto.BookingInitRequest;
import com.Ashish.airBnbClone.dto.GuestDto;
import com.Ashish.airBnbClone.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDto> initialiseBooking(@RequestBody BookingInitRequest bookingInitRequest) {
        return ResponseEntity.ok(bookingService.initialiseBooking(bookingInitRequest));
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addGuests(@PathVariable Long bookingId,
                                                @RequestBody List<GuestDto> guestDtoList) {
        return ResponseEntity.ok(bookingService.addGuests(bookingId, guestDtoList));
    }

}

