package exceptions.custom;

import exceptions.ServiceException;

import java.rmi.ServerException;

public class AdminException extends ServiceException {
  public AdminException() {
  }

  public AdminException(String message) {
    super(message);
  }

  public AdminException(String message, Throwable cause) {
    super(message, cause);
  }
}
