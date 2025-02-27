package repository.custom;

import entity.BookAuthor;
import repository.BookCrudRepo;
import repository.CrudRepository;

import java.sql.SQLException;
import java.util.List;

public interface BookAuthorRepository extends BookCrudRepo<BookAuthor, Integer> {
    public boolean saveList(List<BookAuthor>list) throws SQLException;
    boolean deleteAllWithBookId(Integer bookId) throws SQLException;
    List<BookAuthor> getAll(Integer bookId) throws SQLException;
}
