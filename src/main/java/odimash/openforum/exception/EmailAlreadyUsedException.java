package odimash.openforum.exception;

public class EmailAlreadyUsedException extends RuntimeException {

	public EmailAlreadyUsedException(String email) {
		super(String.format("Email \"%s\" is already used", email));
	}
}
