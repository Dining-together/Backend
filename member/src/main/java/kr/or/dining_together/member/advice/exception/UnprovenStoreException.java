package kr.or.dining_together.member.advice.exception;

public class UnprovenStoreException extends RuntimeException {
	public UnprovenStoreException(String msg, Throwable t) {
		super(msg, t);
	}

	public UnprovenStoreException(String msg) {
		super(msg);
	}

	public UnprovenStoreException() {
		super();
	}
}
