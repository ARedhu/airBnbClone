package com.Ashish.airBnbClone.controller;

import com.Ashish.airBnbClone.dto.BookingDto;
import com.Ashish.airBnbClone.dto.BookingInitRequest;
import com.Ashish.airBnbClone.dto.BookingPaymentInitResponseDto;
import com.Ashish.airBnbClone.dto.GuestDto;
import com.Ashish.airBnbClone.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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

    @PostMapping("/{bookingId}/payments")
    public ResponseEntity<BookingPaymentInitResponseDto> initiatePayment(@PathVariable Long bookingId){
        String sessionUrl = bookingService.initiatePayments(bookingId); // The sessionUrl represents a temporary Stripe Checkout page/session for that specific payment. Stripe server sends this url to -> our backend server sends this url to -> our frontend visits this url for payment
        return ResponseEntity.ok(new BookingPaymentInitResponseDto(sessionUrl));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId){
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

}

