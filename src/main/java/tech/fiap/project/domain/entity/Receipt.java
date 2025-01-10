package tech.fiap.project.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;

@Data
@AllArgsConstructor
public class Receipt {

	private String id;
	private File file;

}
