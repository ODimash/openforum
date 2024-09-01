package odimash.openforum.exception;

@SuppressWarnings("rawtypes")
public class EntityDoesNotExistException extends RuntimeException {
    public EntityDoesNotExistException(Class entity, String name) {
        super(String.format("The entity \"%s\" with name \"%s\" does not exist", entity.getSimpleName(), name));
    }
}
