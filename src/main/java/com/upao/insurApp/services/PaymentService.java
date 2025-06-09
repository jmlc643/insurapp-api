package com.upao.insurApp.services;

import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.upao.insurApp.dto.payment.CreatePaymentDTO;
import com.upao.insurApp.dto.payment.PaymentCaptureResponse;
import com.upao.insurApp.dto.payment.PaymentResponse;
import com.upao.insurApp.dto.paypal.OrderCaptureResponse;
import com.upao.insurApp.dto.paypal.OrderResponse;
import com.upao.insurApp.dto.stripe.ReserveRequest;
import com.upao.insurApp.dto.stripe.StripeResponse;
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
    @Autowired private StripeService stripeService;

    public StripeResponse generatePayment(ReserveRequest request, Integer id) throws StripeException {
        Reserve reserve = reserveRepository.findById(id).orElseThrow(() -> new ResourceNotExistsException("La reserva no existe"));
        Payment payment = new Payment(null, reserve.getTotalPrice(), LocalDateTime.now(), PaymentStatus.PENDING, reserve);
        paymentRepository.save(payment);

        return stripeService.getReserveData(request);
    }

    /*
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
     */

    public Refund refundReserve(String paymentIntent) throws StripeException {
        return stripeService.refundReserve(paymentIntent);
    }
}
