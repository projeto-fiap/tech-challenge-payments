package tech.fiap.project.infra.exception;

import org.springframework.http.HttpStatus;

public class ReceiptNotFound extends BusinessException {

	public ReceiptNotFound(String receiptId) {
		super("receipt.not.found", HttpStatus.NOT_FOUND, null, receiptId);
	}

}
