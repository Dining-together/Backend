package kr.or.dining_together.member.advice.exception;

public class PasswordNotMatchedException extends RuntimeException {
	public PasswordNotMatchedException() {
		super();
	}

	public PasswordNotMatchedException(String message) {
		super(message);
	}

	public PasswordNotMatchedException(String message, Throwable t) {
		super(message, t);
	}
}
