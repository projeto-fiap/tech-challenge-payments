package tech.fiap.project.infra.dataprovider;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.util.ResourceUtils;
import tech.fiap.project.infra.exception.FileNotFound;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReceiptDataProviderImplTest {

	@Mock
	private GridFsTemplate gridFsTemplate;

	@Mock
	private GridFsOperations gridFsOperations;

	@Mock
	private GridFSFile gridFSFile;

	@Mock
	private InputStream inputStream;

	private ReceiptDataProviderImpl receiptDataProviderImpl;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		receiptDataProviderImpl = new ReceiptDataProviderImpl(gridFsTemplate, gridFsOperations);
	}

	@Test
	void testRetrieveFile_shouldReturnFile_whenFileExists() throws Exception {
		String fileId = "123456789012345678901234";
		GridFsResource gridFsResource = mock(GridFsResource.class);
		String path = ResourceUtils.getFile("classpath:TEMP/comprovante_pagamento.pdf").getAbsolutePath();
		if (!Files.exists(Path.of(path))) {
			Files.createFile(Path.of(path));
		}

		when(gridFsTemplate.findOne(any())).thenReturn(gridFSFile);
		when(gridFsOperations.getResource(gridFSFile)).thenReturn(gridFsResource);
		when(gridFsResource.getInputStream()).thenReturn(inputStream);
		when(inputStream.read()).thenReturn(1).thenReturn(-1); // Simula o stream de
																// leitura do arquivo.

		File result = receiptDataProviderImpl.retrieveFile(fileId);

		assertNotNull(result);
		assertTrue(result.exists());
		assertEquals(ResourceUtils.getFile("classpath:TEMP/comprovante_pagamento.pdf").getAbsolutePath(),
				result.getAbsolutePath());
		verify(gridFsTemplate, times(1)).findOne(any());
		verify(gridFsOperations, times(1)).getResource(gridFSFile);
	}

	@Test
	void testRetrieveFile_shouldThrowFileNotFound_whenExceptionOccurs() {
		when(gridFsTemplate.findOne(any())).thenThrow(new RuntimeException("File not found"));
		String fileId = "123456789012345678901234";

		assertThrows(FileNotFound.class, () -> receiptDataProviderImpl.retrieveFile(fileId));
		verify(gridFsTemplate, times(1)).findOne(any());
	}

	@Test
	void testSave_shouldReturnFileId_whenFileSavedSuccessfully() throws Exception {
		String path = ResourceUtils.getFile("classpath:TEMP/comprovante_pagamento.pdf").getAbsolutePath();
		if (!Files.exists(Path.of(path))) {
			Files.createFile(Path.of(path));
		}
		File file = ResourceUtils.getFile("classpath:TEMP/comprovante_pagamento.pdf");
		String expectedFileId = "123456789012345678901234";

		when(gridFsTemplate.store(any(), eq(file.getName()), eq(Files.probeContentType(file.toPath()))))
				.thenReturn(new ObjectId(expectedFileId));

		String result = receiptDataProviderImpl.save(file);

		assertEquals(expectedFileId, result);
		verify(gridFsTemplate, times(1)).store(any(), eq(file.getName()), eq(Files.probeContentType(file.toPath())));
	}

	@Test
	void testSave_shouldThrowException_whenSaveFails() throws Exception {
		String path = ResourceUtils.getFile("classpath:TEMP/comprovante_pagamento.pdf").getAbsolutePath();
		File file;
		if (!Files.exists(Path.of(path))) {
			Path path1 = Files.createFile(Path.of(path));
			file = new ClassPathResource(path1.toAbsolutePath().toString()).getFile();
		}
		else {
			file = ResourceUtils.getFile("classpath:TEMP/comprovante_pagamento.pdf");
		}
		when(gridFsTemplate.store(any(InputStream.class), any(String.class), any(String.class)))
				.thenThrow(new RuntimeException("Error saving file"));

		assertThrows(RuntimeException.class, () -> receiptDataProviderImpl.save(file));
		verify(gridFsTemplate, times(1)).store(any(InputStream.class), any(String.class), any(String.class));
	}

	@Test
	void testStoreFile_shouldReturnFileId_whenFileStored() {
		InputStream fileStream = mock(InputStream.class);
		String fileName = "test-file.pdf";
		String contentType = "application/pdf";
		String expectedFileId = "123456789012345678901234";

		when(gridFsTemplate.store(fileStream, fileName, contentType)).thenReturn(new ObjectId(expectedFileId));

		// Act
		String result = receiptDataProviderImpl.storeFile(fileStream, fileName, contentType);

		// Assert
		assertEquals(expectedFileId, result);
		verify(gridFsTemplate, times(1)).store(fileStream, fileName, contentType);
	}

}
