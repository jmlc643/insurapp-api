package com.upao.insurApp.controllers;

import com.upao.insurApp.dto.payment.CaptureOrderRequest;
import com.upao.insurApp.dto.payment.CreatePaymentDTO;
import com.upao.insurApp.dto.payment.PaymentCaptureResponse;
import com.upao.insurApp.dto.payment.PaymentResponse;
import com.upao.insurApp.services.PaymentService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/payment")
public class PaymentController {
    @Autowired private PaymentService paymentService;

    @PostMapping("")
    public ResponseEntity<PaymentResponse> generatePayment(@RequestBody CreatePaymentDTO request) {
        return new ResponseEntity<>(paymentService.generatePayment(request), HttpStatus.OK);
    }

    @PostMapping("/order")
    public ResponseEntity<PaymentCaptureResponse> captureOrder(@RequestBody CaptureOrderRequest request) throws MessagingException {
        return new ResponseEntity<>(paymentService.captureOrder(request.getOrderId()), HttpStatus.OK);
    }
}
