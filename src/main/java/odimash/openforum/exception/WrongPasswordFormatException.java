package odimash.openforum.exception;

public class WrongPasswordFormatException extends RuntimeException {

	public WrongPasswordFormatException() {
		super("Password is too short");
	}
}
