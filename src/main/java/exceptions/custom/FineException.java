package exceptions.custom;

import exceptions.ServiceException;

public class FineException extends ServiceException {
    public FineException() {
    }

    public FineException(String message) {
        super(message);
    }

    public FineException(String message, Throwable cause) {
        super(message, cause);
    }
}
