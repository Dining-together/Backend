package kr.or.dining_together.auction.advice.exception;

public class NotCompletedException extends RuntimeException {
	public NotCompletedException() {
		super();
	}

	public NotCompletedException(String message) {
		super(message);
	}

	public NotCompletedException(String message, Throwable cause) {
		super(message, cause);
	}
}
