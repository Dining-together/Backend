package kr.or.dining_together.auction.advice.exception;

public class UserNotMatchedException extends RuntimeException {

	public UserNotMatchedException() {
		super();
	}

	public UserNotMatchedException(String message) {
		super(message);
	}

	public UserNotMatchedException(String message, Throwable cause) {
		super(message, cause);
	}
}
