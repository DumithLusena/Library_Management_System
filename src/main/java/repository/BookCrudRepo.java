package repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BookCrudRepo<T,ID> {
    T save(T t) throws SQLException;
    boolean update(T t) throws SQLException;
    boolean delete(ID id) throws SQLException;
    List<T> getAll() throws SQLException;
    Optional<T> search(ID id) throws SQLException;
}
