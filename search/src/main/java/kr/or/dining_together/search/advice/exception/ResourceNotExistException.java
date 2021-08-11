package kr.or.dining_together.search.advice.exception;

public class ResourceNotExistException extends RuntimeException {
	public ResourceNotExistException() {
		super();
	}

	public ResourceNotExistException(String message) {
		super(message);
	}

	public ResourceNotExistException(String message, Throwable cause) {
		super(message, cause);
	}
}
