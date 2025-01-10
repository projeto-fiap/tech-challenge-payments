package tech.fiap.project.app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import tech.fiap.project.app.dto.PaymentDTO;
import tech.fiap.project.app.service.payment.ConfirmPaymentDTOService;
import tech.fiap.project.app.service.payment.RetrievePaymentService;
import tech.fiap.project.domain.entity.Receipt;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class PaymentControllerTest {

	@Mock
	private RetrievePaymentService retrievePaymentService;

	@Mock
	private ConfirmPaymentDTOService confirmPaymentDTOService;

	@InjectMocks
	private PaymentController paymentController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void confirmPayment_shouldReturnPaymentDTO_whenNotFound() throws IOException {
		File file = new File("");
		Receipt receipt = new Receipt("123", file);
		when(confirmPaymentDTOService.confirmPayment(1L)).thenReturn(receipt);
		ResponseEntity<byte[]> response = paymentController.confirmPayment(1L);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void confirmPayment_shouldReturnPaymentDTO_whenSuccessfully() throws IOException {
		File file = ResourceUtils.getFile("classpath:comprovante_pagamento.pdf");
		Receipt receipt = new Receipt("123", file);
		when(confirmPaymentDTOService.confirmPayment(1L)).thenReturn(receipt);
		ResponseEntity<byte[]> response = paymentController.confirmPayment(1L);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void listAll_shouldReturnListOfPaymentDTO_whenSuccessful() {
		List<PaymentDTO> payments = List.of(new PaymentDTO());

		when(retrievePaymentService.findAll()).thenReturn(payments);

		ResponseEntity<List<PaymentDTO>> response = paymentController.listAll();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(payments, response.getBody());
	}

}