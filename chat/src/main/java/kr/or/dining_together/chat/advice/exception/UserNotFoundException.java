package kr.or.dining_together.chat.advice.exception;

public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}

	public UserNotFoundException(String msg) {
		super(msg);
	}

	public UserNotFoundException() {
		super();
	}
}
