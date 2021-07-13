package kr.or.dining_together.auction.advice.exception;

public class UnAuthorizedException extends RuntimeException {
	public UnAuthorizedException(String msg, Throwable t) {
		super(msg, t);
	}

	public UnAuthorizedException(String msg) {
		super(msg);
	}

	public UnAuthorizedException() {
		super();
	}
}
