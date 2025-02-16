package tech.fiap.project.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
@AllArgsConstructor
public class Receipt {

	private String id;

	private File file;

}
