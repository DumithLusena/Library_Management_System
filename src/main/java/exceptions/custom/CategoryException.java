package exceptions.custom;

import exceptions.ServiceException;

public class CategoryException extends ServiceException {
  public CategoryException() {
  }

  public CategoryException(String message) {
    super(message);
  }

  public CategoryException(String message, Throwable cause) {
    super(message, cause);
  }
}
