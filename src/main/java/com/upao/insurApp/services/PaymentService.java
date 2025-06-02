package com.upao.insurApp.services;

import com.upao.insurApp.dto.payment.CreatePaymentDTO;
import com.upao.insurApp.dto.payment.PaymentCaptureResponse;
import com.upao.insurApp.dto.payment.PaymentResponse;
import com.upao.insurApp.dto.paypal.OrderCaptureResponse;
import com.upao.insurApp.dto.paypal.OrderResponse;
import com.upao.insurApp.exceptions.ResourceNotExistsException;
import com.upao.insurApp.models.Payment;
import com.upao.insurApp.models.Reserve;
import com.upao.insurApp.models.enums.PaymentStatus;
import com.upao.insurApp.repos.PaymentRepository;
import com.upao.insurApp.repos.ReserveRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired private PaymentRepository paymentRepository;
    @Autowired private ReserveRepository reserveRepository;
    @Autowired private PayPalService payPalService;

    public PaymentResponse generatePayment(CreatePaymentDTO request) {
        Reserve reserve = reserveRepository.findById(request.getReserveId()).orElseThrow(() -> new ResourceNotExistsException("La reserva no existe"));
        Payment payment = new Payment(null, reserve.getTotalPrice(), LocalDateTime.now(), PaymentStatus.PENDING, reserve);
        paymentRepository.save(payment);
        OrderResponse order = payPalService.createOrder(payment.getPaymentId(), request.getReturnUrl(), request.getCancelUrl());

        String paypalUrl = order
                .getLinks()
                .stream()
                .filter(link -> link.getRel().equals("approve"))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getHref();

        return new PaymentResponse(paypalUrl);
    }

    public PaymentCaptureResponse captureOrder(String orderId) throws MessagingException {
        OrderCaptureResponse orderCapture = payPalService.captureOrder(orderId);
        boolean completed = orderCapture.getStatus().equals("COMPLETED");

        PaymentCaptureResponse paymentCaptureResponse = new PaymentCaptureResponse();
        paymentCaptureResponse.setCompleted(completed);

        if (completed) {
            String purchaseIdStr = orderCapture.getPurchaseUnits().getFirst().getReferenceId();
            confirmPurchase(Integer.parseInt(purchaseIdStr));
            paymentCaptureResponse.setPurchaseId(Integer.parseInt(purchaseIdStr));
            // Enviar email o notificacion
        }

        return paymentCaptureResponse;
    }

    private void confirmPurchase(Integer paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotExistsException("Pago no encontrado"));

        payment.setStatus(PaymentStatus.PAID);
        paymentRepository.save(payment);
    }
}
