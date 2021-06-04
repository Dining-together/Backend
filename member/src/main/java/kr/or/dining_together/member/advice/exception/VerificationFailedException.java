package kr.or.dining_together.member.advice.exception;

public class VerificationFailedException extends RuntimeException {
	public VerificationFailedException(String msg, Throwable t) {
		super(msg, t);
	}

	public VerificationFailedException(String msg) {
		super(msg);
	}

	public VerificationFailedException() {
		super();
	}
}
