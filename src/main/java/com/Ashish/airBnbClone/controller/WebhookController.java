package com.Ashish.airBnbClone.controller;


import com.Ashish.airBnbClone.service.BookingService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final BookingService bookingService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping("/payment")
    public ResponseEntity<String> capturePayments(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader){
        // Stripe sends a JSON payload, but we need the ORIGINAL/raw payload for signature verification. Therefore, don't convert this directly into a DTO.
        // Stripe sends this HTTP header along with the webhook. This signature is used together with endpointSecret to verify the authenticity of the webhook.

        try{
            // Creating an event. It verifies the sigHeader signature with the endpointSecret. Which confirms either this request is coming from Stripe server or not.
            Event event = Webhook.constructEvent(
                    payload,
                    sigHeader,
                    endpointSecret
            );
            // At this point the webhook has been verified.

            bookingService.capturePayment(event);
            return ResponseEntity.ok("Webhook received");

        }catch (SignatureVerificationException e){
            throw new RuntimeException(e);
        }
    }
}
