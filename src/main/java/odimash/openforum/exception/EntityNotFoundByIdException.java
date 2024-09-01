package odimash.openforum.exception;

@SuppressWarnings("rawtypes")
public class EntityNotFoundByIdException extends RuntimeException {
    public EntityNotFoundByIdException(Class entity, Long id) {
        super(String.format("Not found \"%s\" by ID %s", entity.getSimpleName(), id.toString()));
    }
}
