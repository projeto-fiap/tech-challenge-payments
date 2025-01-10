package tech.fiap.project.domain.dataprovider;

import java.io.File;

public interface ReceiptDataProvider {

	File retrieveFile(String fileId);

	String save(File file);

}
