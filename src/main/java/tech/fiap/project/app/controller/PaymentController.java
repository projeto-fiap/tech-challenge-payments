package tech.fiap.project.app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.fiap.project.app.dto.PaymentDTO;
import tech.fiap.project.app.service.payment.ConfirmPaymentDTOService;
import tech.fiap.project.app.service.payment.CreatePaymentService;
import tech.fiap.project.app.service.payment.RetrievePaymentService;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.entity.PaymentQrcode;
import tech.fiap.project.domain.entity.Receipt;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/v1/payments")
@Validated
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final RetrievePaymentService retrievePaymentService;

    private final CreatePaymentService createPaymentService;

    private final ConfirmPaymentDTOService confirmPayment;

    @PostMapping("/confirm/mock/{id}")
    public ResponseEntity<byte[]> confirmPayment(@PathVariable Long id) throws IOException {
        Receipt receipt = confirmPayment.confirmPayment(id);
        File pdfFile = receipt.getFile();
        byte[] pdfBytes;
        if (pdfFile != null && pdfFile.exists()) {
            try (FileInputStream fileInputStream = new FileInputStream(pdfFile)) {
                pdfBytes = fileInputStream.readAllBytes();
                log.info("PDF generated and returned successfully: {}", pdfFile.getName());
            } catch (IOException e) {
                log.error("Error reading PDF file: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", pdfFile.getName());
            headers.add("receiptId", receipt.getId());
            Files.delete(Path.of(pdfFile.getPath()));
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
//				TODO PIX CPF 87520532100
        } else {
            log.error("Failed to generate PDF file.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> listAll() {
        List<PaymentDTO> payments = retrievePaymentService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(payments);
    }

    @PostMapping(produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> createPayment(@RequestBody Order order) {
        PaymentQrcode paymentQrcode = createPaymentService.execute(order);
        BufferedImage qrcode = paymentQrcode.getQrcode();
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.put("paymentId", Collections.singletonList(paymentQrcode.getPayment().getId().toString()));
        return new ResponseEntity<>(qrcode, headers, HttpStatusCode.valueOf(200));
    }
}
