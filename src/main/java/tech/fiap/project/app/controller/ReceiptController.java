package tech.fiap.project.app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.fiap.project.app.service.receipt.RetrieveReceiptService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("api/v1/receipts")
@Validated
@RequiredArgsConstructor
@Slf4j
public class ReceiptController {

	private final RetrieveReceiptService retrieveReceiptService;

	@GetMapping("/{id}")
	public ResponseEntity<byte[]> confirmPayment(@PathVariable String id) {
		File pdfFile = retrieveReceiptService.findById(id);

		if (pdfFile != null && pdfFile.exists()) {
			try (FileInputStream fileInputStream = new FileInputStream(pdfFile)) {
				byte[] pdfBytes = fileInputStream.readAllBytes();

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_PDF);
				headers.setContentDispositionFormData("attachment", pdfFile.getName());
				headers.add("receiptId", id);
				return ResponseEntity.ok().headers(headers).body(pdfBytes);
			}
			catch (IOException e) {
				log.error("Error reading PDF file: {}", e.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}
		else {
			log.error("Failed to generate PDF file.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
