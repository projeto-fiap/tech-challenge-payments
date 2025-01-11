package tech.fiap.project.infra.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ResourceUtils;
import tech.fiap.project.domain.dataprovider.ReceiptDataProvider;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.domain.entity.Receipt;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class GenerateReceiptTest {

	@Mock
	private ReceiptDataProvider receiptDataProvider;

	private GenerateReceipt generateReceipt;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		generateReceipt = new GenerateReceipt(receiptDataProvider);
	}

	@Test
	void testConfirmPayment_shouldGenerateReceiptSuccessfully() throws FileNotFoundException {
		Payment payment = new Payment(1L, null, null, null, null, null, null);
		File file = ResourceUtils.getFile("classpath:TEMP/comprovante_pagamento.pdf");
		String receiptId = "123456789012345678901234";

		when(receiptDataProvider.save(any())).thenReturn(receiptId);
		Receipt receipt = generateReceipt.confirmPayment(payment);
		assertNotNull(receipt);
		assertEquals(receiptId, receipt.getId());
		assertEquals(file, receipt.getFile());
		verify(receiptDataProvider, times(1)).save(any());
	}

	@Test
	void testGenerateHash_shouldReturnExpectedHash() {
		String inputId = "1";
		String expectedHash = "6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b";

		String hash = GenerateReceipt.generateHash(inputId);

		assertEquals(expectedHash, hash);
	}

	@Test
	void testGenerateHash_shouldHandleEmptyInput() {
		String inputId = "";
		String expectedHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

		String hash = GenerateReceipt.generateHash(inputId);

		assertEquals(expectedHash, hash);
	}

}
