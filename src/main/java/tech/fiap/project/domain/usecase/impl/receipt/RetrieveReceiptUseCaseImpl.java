package tech.fiap.project.domain.usecase.impl.receipt;

import tech.fiap.project.domain.dataprovider.ReceiptDataProvider;
import tech.fiap.project.domain.usecase.receipt.RetrieveReceiptUseCase;

import java.io.File;

public class RetrieveReceiptUseCaseImpl implements RetrieveReceiptUseCase {

	private final ReceiptDataProvider receiptDataProvider;

	public RetrieveReceiptUseCaseImpl(ReceiptDataProvider receiptDataProvider) {
		this.receiptDataProvider = receiptDataProvider;
	}


	@Override
	public File findById(String id) {
		return receiptDataProvider.retrieveFile(id);
	}
}
