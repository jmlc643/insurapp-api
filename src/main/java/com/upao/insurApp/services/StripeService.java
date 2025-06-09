package com.upao.insurApp.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.EphemeralKey;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.EphemeralKeyCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import com.upao.insurApp.dto.stripe.ReserveRequest;
import com.upao.insurApp.dto.stripe.StripeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.secretkey}")
    private String secretKey;

    public StripeResponse getReserveData(ReserveRequest reserveRequest) throws StripeException {
        Stripe.apiKey = secretKey;

        // Creacion del objeto Customer
        CustomerCreateParams customerParams = CustomerCreateParams.builder()
                .setName(reserveRequest.getClientName())
                .setPhone(reserveRequest.getPhone())
                .setEmail(reserveRequest.getEmail())
                .build();
        Customer customer = Customer.create(customerParams);

        // Creacion del Ephemeral Key
        EphemeralKeyCreateParams ephemeralKeyParams = EphemeralKeyCreateParams.builder()
                .setStripeVersion("2025-05-28.basil")
                .setCustomer(customer.getId())
                .build();
        EphemeralKey ephemeralKey = EphemeralKey.create(ephemeralKeyParams);

        // Creacion del Payment Intent
        PaymentIntentCreateParams paymentIntentParams = PaymentIntentCreateParams.builder()
                .setAmount(reserveRequest.getAmount())
                .setCurrency("PEN")
                .setCustomer(customer.getId())
                .setDescription(reserveRequest.getProductName())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentParams);

        return new StripeResponse(paymentIntent.getClientSecret(), ephemeralKey.getSecret(), customer.getId(), paymentIntent.getId());
    }

    public Refund refundReserve(String paymentIntent) throws StripeException {
        Stripe.apiKey = secretKey;

        RefundCreateParams refundParams = RefundCreateParams.builder()
                .setPaymentIntent(paymentIntent)
                .build();

        return Refund.create(refundParams);
    }
}
