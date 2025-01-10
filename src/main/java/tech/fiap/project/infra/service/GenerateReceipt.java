package tech.fiap.project.infra.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import tech.fiap.project.domain.dataprovider.ReceiptDataProvider;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.domain.entity.Receipt;
import tech.fiap.project.domain.usecase.GenerateReceiptUseCase;
import tech.fiap.project.infra.exception.FileNotFound;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class GenerateReceipt implements GenerateReceiptUseCase {

	private File file = null ;

	private ReceiptDataProvider receiptDataProvider;

	public GenerateReceipt(ReceiptDataProvider receiptDataProvider) {
		this.receiptDataProvider = receiptDataProvider;
	}

	@Override
	public Receipt confirmPayment(Payment payment) {
		LocalDateTime dataHoraAtual = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String dataHoraFormatada = dataHoraAtual.format(formatter);
		try {
		File logoFile = ResourceUtils.getFile("classpath:logo_fiap.png");
		String absolutePath = ResourceUtils.getFile("classpath:TEMP").getAbsolutePath() + "/comprovante_pagamento.pdf";
			file = new File(absolutePath);
			PdfWriter writer = new PdfWriter(file);
			PdfDocument pdfDoc = new PdfDocument(writer);
			Document document = new Document(pdfDoc);

			Color tituloCor = new DeviceRgb(237, 25, 92); // Vermelho vibrante
			Color textoCor = new DeviceRgb(34, 49, 63);  // Azul escuro

			document.add(new Paragraph("Fiap Tech Challenge")
					.setFontSize(20)
					.setFontColor(tituloCor)
					.setBold()
					.setTextAlignment(TextAlignment.CENTER));

			document.add(new Paragraph("Comprovante de Pagamento")
					.setFontSize(16)
					.setBold()
					.setTextAlignment(TextAlignment.CENTER)
					.setMarginBottom(20));

			document.add(new Paragraph("Data e Hora: " + dataHoraFormatada)
					.setFontSize(12)
					.setFontColor(textoCor)
					.setTextAlignment(TextAlignment.LEFT)
					.setMarginBottom(10));


			document.add(new Paragraph("Valor Pago: R$ " + String.format("%.2f", payment.getAmount()))
					.setFontSize(12)
					.setFontColor(textoCor)
					.setTextAlignment(TextAlignment.LEFT)
					.setMarginBottom(10));

			document.add(new Paragraph("Método de pagamento: ")
					.setFontSize(12)
					.setFontColor(textoCor)
					.add(new Paragraph("Pix").setBold()));

			document.add(new Paragraph(String.format("Código de Autenticação: %s",generateHash(payment.getId().toString())))
					.setFontSize(12)
					.setFontColor(textoCor)
					.setTextAlignment(TextAlignment.LEFT)
					.setMarginBottom(10));

			document.add(new Paragraph("Obrigado por utilizar nossos serviços!")
					.setFontSize(12)
					.setFontColor(textoCor)
					.setTextAlignment(TextAlignment.LEFT));

			document.add(new Paragraph("Bom apetite!")
					.setFontSize(14)
					.setBold()
					.setFontColor(tituloCor)
					.setTextAlignment(TextAlignment.CENTER)
					.setMarginTop(20));

			document.add(new Paragraph("").setMarginTop(80));


			try {
				ImageData imageData = ImageDataFactory.create(logoFile.getAbsolutePath());
				Image logo = new Image(imageData);
				logo.setWidth(600);
				logo.setHeight(300);
				logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
				document.add(logo);
			} catch (Exception e) {
				System.out.println("Imagem não encontrada na pasta resources: " + e.getMessage());
			}
			document.close();
			String id = receiptDataProvider.save(file);
			return new Receipt(id,file);
		} catch (FileNotFoundException e) {
            throw new FileNotFound(e);
        }
    }

	public static String generateHash(String id) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = md.digest(id.getBytes(StandardCharsets.UTF_8));
			BigInteger number = new BigInteger(1, hashBytes);
			StringBuilder hashText = new StringBuilder(number.toString(16));

			while (hashText.length() < 64) {
				hashText.insert(0, '0');
			}

			return hashText.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error generating hash: " + e.getMessage());
		}
	}
}
