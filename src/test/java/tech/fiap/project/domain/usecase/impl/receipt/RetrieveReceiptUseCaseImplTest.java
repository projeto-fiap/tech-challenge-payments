package tech.fiap.project.domain.usecase.impl.receipt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.fiap.project.domain.dataprovider.ReceiptDataProvider;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RetrieveReceiptUseCaseImplTest {

	@InjectMocks
	private RetrieveReceiptUseCaseImpl retrieveReceiptUseCase;

	@Mock
	private ReceiptDataProvider receiptDataProvider;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void findById_shouldReturnFileWhenFound() {
		String mockId = "123";
		File mockFile = new File("mockFile.pdf");
		when(receiptDataProvider.retrieveFile(mockId)).thenReturn(mockFile);

		File result = retrieveReceiptUseCase.findById(mockId);

		assertNotNull(result);
		assertEquals(mockFile, result);
		verify(receiptDataProvider, times(1)).retrieveFile(mockId);
	}

	@Test
	void findById_shouldReturnNullWhenFileNotFound() {
		String mockId = "123";
		when(receiptDataProvider.retrieveFile(mockId)).thenReturn(null);

		File result = retrieveReceiptUseCase.findById(mockId);

		assertNull(result);
		verify(receiptDataProvider, times(1)).retrieveFile(mockId);
	}

	@Test
	void findById_shouldThrowExceptionWhenDataProviderThrows() {
		String mockId = "123";
		when(receiptDataProvider.retrieveFile(mockId)).thenThrow(new RuntimeException("File retrieval failed"));

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			retrieveReceiptUseCase.findById(mockId);
		});

		assertEquals("File retrieval failed", exception.getMessage());
		verify(receiptDataProvider, times(1)).retrieveFile(mockId);
	}

}
