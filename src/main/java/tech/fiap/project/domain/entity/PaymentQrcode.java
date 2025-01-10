package tech.fiap.project.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

@Setter
@Getter
@AllArgsConstructor
public class PaymentQrcode {

	private Payment payment;

	private BufferedImage qrcode;

}
