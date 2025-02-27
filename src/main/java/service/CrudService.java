package service;

import exceptions.ServiceException;

import java.util.List;
import java.util.Optional;

public interface CrudService<T,ID> extends SuperService{
    boolean add(T t) throws ServiceException;
    boolean update(T t) throws ServiceException;
    boolean delete(ID id) throws ServiceException;
    Optional<T> search(ID id) throws ServiceException;
    List<T> getAll() throws ServiceException;
}
