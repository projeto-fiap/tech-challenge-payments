package tech.fiap.project.infra.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomExceptionHandlerTest {

	@InjectMocks
	private CustomExceptionHandler customExceptionHandler;

	@Mock
	private WebRequest webRequest;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void handleConflict_shouldReturnResponseEntity() {
		// Arrange
		BusinessException businessException = mock(BusinessException.class);
		HttpStatusCode httpStatusCode = HttpStatusCode.valueOf(409);
		when(businessException.getHttpStatusCode()).thenReturn(httpStatusCode);
		when(businessException.getMessage()).thenReturn("Conflict occurred");
		when(businessException.getMetadata()).thenReturn(null);

		// Act
		ResponseEntity<Object> responseEntity = customExceptionHandler.handleConflict(businessException, webRequest);

		// Assert
		ExceptionResponse expectedResponse = new ExceptionResponse("Conflict occurred", 409, "409 CONFLICT", null);
		assertTrue(Objects.requireNonNull(responseEntity.getBody()).toString().contains(expectedResponse.getMessage()));
		assertEquals(httpStatusCode, responseEntity.getStatusCode());
	}

	@Test
	void handleConflict_withMetadata_shouldReturnResponseEntity() {
		// Arrange
		BusinessException businessException = mock(BusinessException.class);
		HttpStatusCode httpStatusCode = HttpStatusCode.valueOf(409);
		when(businessException.getHttpStatusCode()).thenReturn(httpStatusCode);
		when(businessException.getMessage()).thenReturn("Conflict occurred");
		when(businessException.getMetadata()).thenReturn("Some metadata");

		// Act
		ResponseEntity<Object> responseEntity = customExceptionHandler.handleConflict(businessException, webRequest);

		// Assert
		ExceptionResponse expectedResponse = new ExceptionResponse("Conflict occurred", 409, "409 CONFLICT",
				"Some metadata");
		assertTrue(Objects.requireNonNull(responseEntity.getBody()).toString().contains(expectedResponse.getMessage()));
		assertEquals(httpStatusCode, responseEntity.getStatusCode());
	}

}