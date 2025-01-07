package tech.fiap.project.app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.fiap.project.app.dto.ConfirmPaymentDTO;
import tech.fiap.project.app.dto.PaymentDTO;
import tech.fiap.project.app.service.payment.ConfirmPaymentDTOService;
import tech.fiap.project.app.service.payment.RetrievePaymentService;

import java.util.List;

@RestController
@RequestMapping("api/v1/payments")
@Validated
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

	private final RetrievePaymentService retrievePaymentService;

	private final ConfirmPaymentDTOService confirmPaymentDTOService;

	@PostMapping("/confirm/mock")
	public ResponseEntity<PaymentDTO> confirmPayment(@RequestBody @Validated ConfirmPaymentDTO confirmPaymentDTO) {
		log.info("Received request to create items: {}", confirmPaymentDTO);
		PaymentDTO paymentDTO = confirmPaymentDTOService.confirmPayment(confirmPaymentDTO);
		log.info("Items created successfully: {}", paymentDTO);
		return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
	}

	@GetMapping
	public ResponseEntity<List<PaymentDTO>> listAll() {
		List<PaymentDTO> payments = retrievePaymentService.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(payments);
	}

}
