package kr.or.dining_together.member.advice.exception;

public class DataSaveFailedException extends RuntimeException {
	public DataSaveFailedException(String msg, Throwable t) {
		super(msg, t);
	}

	public DataSaveFailedException(String msg) {
		super(msg);
	}

	public DataSaveFailedException() {
		super();
	}
}
