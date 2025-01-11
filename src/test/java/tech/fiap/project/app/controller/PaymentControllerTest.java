package tech.fiap.project.app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tech.fiap.project.app.dto.PaymentDTO;
import tech.fiap.project.app.service.payment.ConfirmPaymentDTOService;
import tech.fiap.project.app.service.payment.CreatePaymentService;
import tech.fiap.project.app.service.payment.RetrievePaymentService;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.domain.entity.PaymentQrcode;
import tech.fiap.project.domain.entity.Receipt;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

	@InjectMocks
	private PaymentController paymentController;

	@Mock
	private RetrievePaymentService retrievePaymentService;

	@Mock
	private CreatePaymentService createPaymentService;

	@Mock
	private ConfirmPaymentDTOService confirmPaymentDTOService;

	private File tempPdfFile;

	@BeforeEach
	void setUp() throws IOException {
		MockitoAnnotations.openMocks(this);

		tempPdfFile = File.createTempFile("test_receipt", ".pdf");
		try (FileOutputStream fos = new FileOutputStream(tempPdfFile)) {
			fos.write("PDF content".getBytes());
		}
	}

	@Test
	void confirmPayment_shouldReturnPdf_whenFileExists() throws IOException {
		Receipt receipt = new Receipt("123", tempPdfFile);
		when(confirmPaymentDTOService.confirmPayment(1L)).thenReturn(receipt);

		ResponseEntity<byte[]> response = paymentController.confirmPayment(1L);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("form-data; name=\"attachment\"; filename=\"" + tempPdfFile.getName() + "\"",
				response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0));
		assertEquals("123", response.getHeaders().get("receiptId").get(0));
		verify(confirmPaymentDTOService, times(1)).confirmPayment(1L);
	}

	@Test
	void confirmPayment_shouldReturnNotFound_whenFileDoesNotExist() throws IOException {
		Receipt receipt = new Receipt("123", null);
		when(confirmPaymentDTOService.confirmPayment(1L)).thenReturn(receipt);

		ResponseEntity<byte[]> response = paymentController.confirmPayment(1L);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		verify(confirmPaymentDTOService, times(1)).confirmPayment(1L);
	}

	@Test
	void listAll_shouldReturnListOfPayments() {
		PaymentDTO paymentDTO = new PaymentDTO();
		when(retrievePaymentService.findAll()).thenReturn(List.of(paymentDTO));

		ResponseEntity<List<PaymentDTO>> response = paymentController.listAll();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(1, response.getBody().size());
		verify(retrievePaymentService, times(1)).findAll();
	}

	@Test
	void createPayment_shouldReturnQRCodeAndHeaders() {
		BufferedImage qrcode = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		Payment payment = new Payment(1L, null, null, null, null, null, null);
		PaymentQrcode paymentQrcode = new PaymentQrcode(payment, qrcode);
		when(createPaymentService.execute(any(Order.class))).thenReturn(paymentQrcode);

		Order order = new Order();
		ResponseEntity<BufferedImage> response = paymentController.createPayment(order);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("1", response.getHeaders().get("paymentId").get(0));
		verify(createPaymentService, times(1)).execute(order);
	}

}
