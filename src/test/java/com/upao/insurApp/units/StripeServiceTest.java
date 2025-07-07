package com.upao.insurApp.units;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.EphemeralKey;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.EphemeralKeyCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.upao.insurApp.dto.stripe.ReserveRequest;
import com.upao.insurApp.dto.stripe.StripeResponse;
import com.upao.insurApp.services.StripeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.cloudinary.AccessControlRule.AccessType.token;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class StripeServiceTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @InjectMocks private StripeService stripeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stripeService = new StripeService();
    }

    // Verifica que el endpoint de pago simulado funcione correctamente
    @Test
    void shouldSimulateStripePayment() throws Exception {

        // Paso 1: Crear un request Stripe simulado
        ReserveRequest request = new ReserveRequest();
        request.setClientName("Juan Pérez");
        request.setEmail("juan@example.com");
        request.setPhone("999999999");
        request.setAmount(2000L); // monto en centavos
        request.setProductName("Reserva de cancha");

        // Paso 2: Convertir a JSON
        String jsonRequest = objectMapper.writeValueAsString(request);

        // Paso 3: Ejecutar el POST al endpoint de pago
        mockMvc.perform(MockMvcRequestBuilders.post("/api/payment/reserve/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientSecret").exists())
                .andExpect(jsonPath("$.paymentId").exists())
                .andDo(print());
    }

    @Test
    void shouldReturnStripeResponse() throws StripeException {
        // Arrange
        ReserveRequest reserveRequest = new ReserveRequest();
        reserveRequest.setClientName("Juan Pérez");
        reserveRequest.setEmail("juan@example.com");
        reserveRequest.setPhone("999999999");
        reserveRequest.setAmount(2000L);
        reserveRequest.setProductName("Reserva de cancha");

        // Simular el comportamiento de Stripe usando Mockito
        try (MockedStatic<Customer> customerMockedStatic = mockStatic(Customer.class);
             MockedStatic<EphemeralKey> ephemeralKeyMockedStatic = mockStatic(EphemeralKey.class);
             MockedStatic<PaymentIntent> paymentIntentMockedStatic = mockStatic(PaymentIntent.class)) {

            // Simular la creación de Customer, EphemeralKey y PaymentIntent
            Customer customerMock = mock(Customer.class);
            EphemeralKey ephemeralKeyMock = mock(EphemeralKey.class);
            PaymentIntent paymentIntentMock = mock(PaymentIntent.class);

            // Imita las llamadas a la API de Stripe

            // Configurar el comportamiento simulado
            customerMockedStatic.when(() -> Customer.create(any(CustomerCreateParams.class))).thenReturn(customerMock);
            when(customerMock.getId()).thenReturn("cus_A1B2C3D4E5"); // Id de un cliente simulado

            // Simular la creación de una clave efímera
            ephemeralKeyMockedStatic.when(() -> EphemeralKey.create(any(EphemeralKeyCreateParams.class))).thenReturn(ephemeralKeyMock);
            when(ephemeralKeyMock.getSecret()).thenReturn("ek_test_ABC123XYZ456"); // Id secreta de una clave efímera simulada

            // Simular la creación de un PaymentIntent
            paymentIntentMockedStatic.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class))).thenReturn(paymentIntentMock);
            when(paymentIntentMock.getClientSecret()).thenReturn("pi_ABC123_secret_XYZ456"); // Clave secreta del PaymentIntent simulado
            when(paymentIntentMock.getId()).thenReturn("pi_ABC123XYZ456"); // Id del PaymentIntent simulado

            // Act
            StripeResponse response = stripeService.getReserveData(reserveRequest, 1);

            // Assert
            assertNotNull(response);
            assertNotNull(response.getClientSecret());
            assertNotNull(response.getPaymentId());
        }
    }
}
