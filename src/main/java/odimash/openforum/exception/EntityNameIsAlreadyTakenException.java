package odimash.openforum.exception;

@SuppressWarnings("rawtypes")
public class EntityNameIsAlreadyTakenException extends RuntimeException {
    public EntityNameIsAlreadyTakenException(Class entity, String name) {
        super(String.format("The name \"%s\" of the \"%s\" is already taken", name, entity.getSimpleName()));
    }
}
