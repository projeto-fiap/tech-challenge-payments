package tech.fiap.project.app.service.receipt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.fiap.project.domain.usecase.receipt.RetrieveReceiptUseCase;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RetrieveReceiptServiceTest {

	@Mock
	private RetrieveReceiptUseCase retrieveReceiptUseCase;

	private RetrieveReceiptService retrieveReceiptService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this); // Inicializa os mocks
		retrieveReceiptService = new RetrieveReceiptService(retrieveReceiptUseCase);
	}

	@Test
	void testFindById_shouldReturnFile_whenValidId() {
		String receiptId = "123";
		File expectedFile = new File("path/to/receipt.pdf");
		when(retrieveReceiptUseCase.findById(receiptId)).thenReturn(expectedFile);

		File result = retrieveReceiptService.findById(receiptId);
		assertNotNull(result);
		assertEquals(expectedFile, result);
		verify(retrieveReceiptUseCase, times(1)).findById(receiptId); // Verifica se o
																		// método foi
																		// chamado uma vez
	}

	@Test
	void testFindById_shouldReturnNull_whenReceiptNotFound() {
		String receiptId = "999";
		when(retrieveReceiptUseCase.findById(receiptId)).thenReturn(null);

		File result = retrieveReceiptService.findById(receiptId);

		assertNull(result);
		verify(retrieveReceiptUseCase, times(1)).findById(receiptId); // Verifica se o
																		// método foi
																		// chamado uma vez
	}

	@Test
	void testFindById_shouldHandleException() {
		String receiptId = "123";
		when(retrieveReceiptUseCase.findById(receiptId)).thenThrow(new RuntimeException("Error retrieving receipt"));

		Exception exception = assertThrows(RuntimeException.class, () -> {
			retrieveReceiptService.findById(receiptId);
		});

		assertEquals("Error retrieving receipt", exception.getMessage());
		verify(retrieveReceiptUseCase, times(1)).findById(receiptId); // Verifica se o
																		// método foi
																		// chamado uma vez
	}

	@Test
	void testFindById_shouldCallFindByIdOnce() {
		String receiptId = "123";
		File expectedFile = new File("path/to/receipt.pdf");
		when(retrieveReceiptUseCase.findById(receiptId)).thenReturn(expectedFile);

		retrieveReceiptService.findById(receiptId);

		verify(retrieveReceiptUseCase, times(1)).findById(receiptId); // Verifica se o
																		// método foi
																		// chamado uma vez
	}

}
