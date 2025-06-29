package com.upao.insurApp.controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.upao.insurApp.dto.stripe.ReserveRequest;
import com.upao.insurApp.dto.stripe.StripeResponse;
import com.upao.insurApp.services.PaymentService;
import com.upao.insurApp.services.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payment")
public class PaymentController {
    @Autowired private PaymentService paymentService;
    @Autowired private StripeService stripeService;

    @PostMapping("/reserve/{id}")
    public ResponseEntity<StripeResponse> getReserveData(@RequestBody ReserveRequest request, @PathVariable Integer id) throws StripeException {
        return new ResponseEntity<>(paymentService.generatePayment(request, id), HttpStatus.OK);
    }

    @PatchMapping("/status/{paymentId}")
    public ResponseEntity<Void> updatePaymentStatus(@PathVariable Integer paymentId, @RequestParam("status") String status) {
        return new ResponseEntity<>(paymentService.updatePaymentStatus(paymentId, status), HttpStatus.NO_CONTENT);
    }

    @PostMapping("/refund/{clientSecret}")
    public ResponseEntity<Refund> refundReserve(@PathVariable String clientSecret) throws StripeException {
        return new ResponseEntity<>(stripeService.refundReserve(clientSecret), HttpStatus.OK);
    }
}
