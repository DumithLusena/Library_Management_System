package exceptions.custom;

import exceptions.ServiceException;

public class BorrowBookRecordsException extends ServiceException {
    public BorrowBookRecordsException() {
    }

    public BorrowBookRecordsException(String message) {
        super(message);
    }

    public BorrowBookRecordsException(String message, Throwable cause) {
        super(message, cause);
    }
}
