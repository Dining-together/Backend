package kr.or.dining_together.member.advice.exception;

public class CComunicationException extends RuntimeException{
	public CComunicationException() {
		super();
	}

	public CComunicationException(String message) {
		super(message);
	}

	public CComunicationException(String message, Throwable cause) {
		super(message, cause);
	}
}
