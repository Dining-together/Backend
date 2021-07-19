package kr.or.dining_together.member.advice.exception;

public class FileNotFoundException extends RuntimeException {
	public FileNotFoundException() {
		super();
	}

	public FileNotFoundException(String message) {
		super(message);
	}

	public FileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
