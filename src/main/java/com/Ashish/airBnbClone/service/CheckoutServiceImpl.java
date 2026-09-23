package com.Ashish.airBnbClone.service;

import com.Ashish.airBnbClone.entity.Booking;
import com.Ashish.airBnbClone.entity.User;
import com.Ashish.airBnbClone.repository.BookingRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
@Slf4j
public class CheckoutServiceImpl implements CheckoutService{

    private final BookingRepository bookingRepository;

    @Override
    public String getCheckoutSession(Booking booking, String successUrl, String failureUrl) {
        log.info("Creating session for booking with ID: {}", booking.getId());

        try{
            Customer customer = createCustomer(booking);
            Session session = createStripeSession(booking, customer, successUrl, failureUrl);
            savePaymentSessionId(booking, session);

            log.info("Session created successfully for booking with ID: {}", booking.getId());
            return session.getUrl();
        } catch (StripeException e){
            throw new RuntimeException(e);
        }

    }

    private Customer createCustomer(Booking booking) throws StripeException {
        CustomerCreateParams customerParams = CustomerCreateParams.builder()
                .setName(booking.getUser().getName())
                .setEmail(booking.getUser().getEmail())
                .build();

        return Customer.create(customerParams);
    }

    private Session createStripeSession(Booking booking, Customer customer, String successUrl, String failureUrl) throws StripeException {
        SessionCreateParams sessionParams = SessionCreateParams.builder()
                                                .setMode(SessionCreateParams.Mode.PAYMENT)
                                                .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                                                .setCustomer(customer.getId())
                                                .setSuccessUrl(successUrl)
                                                .setCancelUrl(failureUrl)
                                                .addLineItem(buildLineItem(booking))
                                                .build();

        return Session.create(sessionParams);
    }

    private SessionCreateParams.LineItem buildLineItem(Booking booking){
        return SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(buildPriceData(booking))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData buildPriceData(Booking booking){
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("inr")
                .setUnitAmount(
                        booking.getAmount()
                                .multiply(BigDecimal.valueOf(100))
                                .longValue()
                ) // Bydefault it take the lowest possible unit of currency. So, it will consider in paise. So, we will multiply by 100. So that it can convert that into ruppees.
                .setProductData(buildProductData(booking))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData buildProductData(Booking booking){
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(
                        booking.getHotel().getName()
                        + " : "
                        + booking.getRoom().getType()
                )
                .setDescription("Booking Id: "+booking.getId())
                .build();
    }

    private void savePaymentSessionId(Booking booking, Session session){
        booking.setPaymentSessionId(session.getId());
        bookingRepository.save(booking);
    }
}
