package kr.or.dining_together.member.advice.exception;

public class loginFailedException extends RuntimeException  {
    public loginFailedException(String msg, Throwable t) {
        super(msg, t);
    }

    public loginFailedException(String msg) {
        super(msg);
    }

    public loginFailedException() {
        super();
    }
}
