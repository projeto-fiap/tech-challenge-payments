package tech.fiap.project.domain.usecase.impl.payment;

import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.domain.entity.PaymentQrcode;
import tech.fiap.project.domain.usecase.CreateQrCodeUseCase;
import tech.fiap.project.domain.usecase.payment.CreatePayment;
import tech.fiap.project.domain.usecase.payment.CreatePaymentUseCase;

import java.awt.image.BufferedImage;

public class CreatePaymentUseCaseImpl implements CreatePaymentUseCase {

	private final CreateQrCodeUseCase generateQrCode;

	private final CreatePayment createPayment;

	public CreatePaymentUseCaseImpl(CreateQrCodeUseCase generateQrCode, CreatePayment createPayment) {
		this.generateQrCode = generateQrCode;
		this.createPayment = createPayment;
	}

	public PaymentQrcode execute(Order order) {
		Payment payment = createPayment.execute(order);
		BufferedImage qrcode = generateQrCode.execute(order);
		return new PaymentQrcode(payment, qrcode);
	}

}
