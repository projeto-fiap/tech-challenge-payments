package tech.fiap.project.domain.usecase.receipt;

import java.io.File;

public interface RetrieveReceiptUseCase {

	File findById(String id);

}
