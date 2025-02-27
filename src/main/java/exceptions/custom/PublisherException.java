package exceptions.custom;

import exceptions.ServiceException;

public class PublisherException extends ServiceException {
  public PublisherException() {
  }

  public PublisherException(String message) {
    super(message);
  }

  public PublisherException(String message, Throwable cause) {
    super(message, cause);
  }

}

