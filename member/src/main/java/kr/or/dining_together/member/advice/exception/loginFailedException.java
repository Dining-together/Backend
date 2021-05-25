package kr.or.dining_together.member.advice.exception;

public class LoginFailedException extends RuntimeException  {
    public LoginFailedException(String msg, Throwable t) {
        super(msg, t);
    }

    public LoginFailedException(String msg) {
        super(msg);
    }

    public LoginFailedException() {
        super();
    }
}
