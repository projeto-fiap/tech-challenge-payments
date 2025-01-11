package tech.fiap.project.app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import tech.fiap.project.app.service.receipt.RetrieveReceiptService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ReceiptControllerTest {

	@InjectMocks
	private ReceiptController receiptController;

	@Mock
	private RetrieveReceiptService retrieveReceiptService;

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
	void confirmPayment_shouldReturnPdf_whenFileExists() {
		when(retrieveReceiptService.findById("123")).thenReturn(tempPdfFile);

		ResponseEntity<byte[]> response = receiptController.confirmPayment("123");

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
		assertEquals("form-data; name=\"attachment\"; filename=\"" + tempPdfFile.getName() + "\"",
				response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0));
		assertEquals("123", response.getHeaders().get("receiptId").get(0));
		verify(retrieveReceiptService, times(1)).findById("123");
	}

	@Test
	void confirmPayment_shouldReturnInternalServerError_whenFileDoesNotExist() {
		File nonExistentFile = new File("non_existent_file.pdf");
		when(retrieveReceiptService.findById("123")).thenReturn(nonExistentFile);

		ResponseEntity<byte[]> response = receiptController.confirmPayment("123");

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		verify(retrieveReceiptService, times(1)).findById("123");
	}

	@Test
	void confirmPayment_shouldReturnInternalServerError_whenFileIsNull() {
		when(retrieveReceiptService.findById("123")).thenReturn(null);

		ResponseEntity<byte[]> response = receiptController.confirmPayment("123");

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		verify(retrieveReceiptService, times(1)).findById("123");
	}

}
