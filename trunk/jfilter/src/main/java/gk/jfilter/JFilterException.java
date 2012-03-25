package gk.jfilter;
public class JFilterException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public JFilterException() {
		super();
	}

	public JFilterException(String message) {
		super(message);
	}

	public JFilterException(String message, Throwable cause) {
		super(message, cause);
	}

	public JFilterException(Throwable cause) {
		super(cause);
	}
}
