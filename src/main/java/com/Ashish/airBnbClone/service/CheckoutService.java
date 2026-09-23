package com.Ashish.airBnbClone.service;

import com.Ashish.airBnbClone.entity.Booking;

public interface CheckoutService {
    String getCheckoutSession(Booking booking, String successUrl, String failureUrl);
}
