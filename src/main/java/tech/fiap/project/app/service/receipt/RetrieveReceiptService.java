package tech.fiap.project.app.service.receipt;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.fiap.project.domain.usecase.receipt.RetrieveReceiptUseCase;

import java.io.File;

@AllArgsConstructor
@Service
public class RetrieveReceiptService {

	private RetrieveReceiptUseCase retrieveReceiptUseCase;

	public File findById(String id) {
		return retrieveReceiptUseCase.findById(id);
	}

}
