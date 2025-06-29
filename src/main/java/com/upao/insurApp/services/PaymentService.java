package com.upao.insurApp.services;

import com.stripe.exception.StripeException;
import com.upao.insurApp.dto.stripe.ReserveRequest;
import com.upao.insurApp.dto.stripe.StripeResponse;
import com.upao.insurApp.exceptions.ResourceNotExistsException;
import com.upao.insurApp.models.Payment;
import com.upao.insurApp.models.Reserve;
import com.upao.insurApp.models.enums.PaymentStatus;
import com.upao.insurApp.repos.PaymentRepository;
import com.upao.insurApp.repos.ReserveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired private PaymentRepository paymentRepository;
    @Autowired private ReserveRepository reserveRepository;
    @Autowired private StripeService stripeService;

    public StripeResponse generatePayment(ReserveRequest request, Integer id) throws StripeException {
        Reserve reserve = reserveRepository.findById(id).orElseThrow(() -> new ResourceNotExistsException("La reserva no existe"));
        Payment payment = new Payment(null, reserve.getTotalPrice(), LocalDateTime.now(), PaymentStatus.PENDING, reserve);
        paymentRepository.save(payment);

        return stripeService.getReserveData(request, payment.getPaymentId());
    }

    public Void updatePaymentStatus(Integer paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotExistsException("El pago no existe"));
        PaymentStatus paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
        payment.setStatus(paymentStatus);
        paymentRepository.save(payment);
        return null;
    }
}
