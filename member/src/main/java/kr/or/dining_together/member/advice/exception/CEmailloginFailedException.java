package kr.or.dining_together.member.advice.exception;

public class CEmailloginFailedException extends RuntimeException  {
    public CEmailloginFailedException(String msg, Throwable t) {
        super(msg, t);
    }

    public CEmailloginFailedException(String msg) {
        super(msg);
    }

    public CEmailloginFailedException() {
        super();
    }
}
