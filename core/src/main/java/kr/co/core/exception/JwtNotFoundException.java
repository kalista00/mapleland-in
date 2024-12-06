package kr.co.core.exception;

public class JwtNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JwtNotFoundException() {
		super("JWT not found in cookies");
	}

	public JwtNotFoundException(String message) {
		super(message);
	}

	public JwtNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}