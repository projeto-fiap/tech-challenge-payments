package tech.fiap.project.infra.exception;

public class FileNotFound extends RuntimeException {

	public FileNotFound(Throwable throwable) {
		super(throwable);
	}

}
