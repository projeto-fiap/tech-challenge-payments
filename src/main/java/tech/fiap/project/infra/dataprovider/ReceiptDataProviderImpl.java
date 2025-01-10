package tech.fiap.project.infra.dataprovider;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import tech.fiap.project.domain.dataprovider.ReceiptDataProvider;
import tech.fiap.project.infra.exception.FileNotFound;

import java.io.File;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@AllArgsConstructor
@Slf4j
public class ReceiptDataProviderImpl implements ReceiptDataProvider {

	private final GridFsTemplate gridFsTemplate;

	private final GridFsOperations gridFsOperations;

	public File retrieveFile(String fileId) {
		try {
			GridFSFile receipt = findFileById(fileId);
			GridFsResource resource = gridFsOperations.getResource(receipt);
			String absolutePath = ResourceUtils.getFile("classpath:TEMP").getAbsolutePath()
					+ "/comprovante_pagamento.pdf";
			File file = new File(absolutePath);
			try {
				Files.copy(resource.getInputStream(), file.toPath());
			}
			catch (FileAlreadyExistsException e) {
				log.info("Arquivo {} ja existe", file);
			}
			return file;
		}
		catch (Exception e) {
			throw new FileNotFound(e);
		}
	}

	public GridFSFile findFileById(String fileId) {
		ObjectId objectId = new ObjectId(fileId);
		return gridFsTemplate.findOne(query(where("_id").is(objectId)));
	}

	@Override
	public String save(File file) {
		try (InputStream fileStream = Files.newInputStream(file.toPath())) {
			return storeFile(fileStream, file.getName(), Files.probeContentType(file.toPath()));
		}
		catch (Exception e) {
			throw new RuntimeException("Erro ao salvar o arquivo", e);
		}
	}

	public String storeFile(InputStream fileStream, String fileName, String contentType) {
		ObjectId fileId = gridFsTemplate.store(fileStream, fileName, contentType);
		return fileId.toString();
	}

}
