package repository.custom.impl;

import db.util.CrudUtil;
import entity.BookAuthor;
import repository.custom.BookAuthorRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookAuthorRepositoryIMPL implements BookAuthorRepository {

    boolean flag = true;

    @Override
    public boolean saveList(List<BookAuthor> list) throws SQLException {
        flag = true;
        for (BookAuthor bookAuthor : list) {
            if (flag){
                save(bookAuthor);
            }else{
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean deleteAllWithBookId(Integer bookId) throws SQLException {
        String SQL = "DELETE FROM book_author WHERE book_id = ?";
        return CrudUtil.execute(SQL, bookId);
    }

    @Override
    public List<BookAuthor> getAll(Integer bookId) throws SQLException {
        String SQL = "SELECT * FROM book_author WHERE book_id = ?";
        ResultSet resultSet = CrudUtil.execute(SQL, bookId);
        List<BookAuthor> list = new ArrayList<>();
        while (resultSet.next()){
            BookAuthor bookAuthor = new BookAuthor();
            bookAuthor.setBookId(resultSet.getInt(1));
            bookAuthor.setAuthorId(resultSet.getInt(2));
            list.add(bookAuthor);
        }
        return list;
    }

    @Override
    public BookAuthor save(BookAuthor bookAuthor) throws SQLException {
        String SQL = "INSERT INTO book_author(book_id,author_id) VALUES(?,?)";
        flag = CrudUtil.execute(SQL, bookAuthor.getBookId(), bookAuthor.getAuthorId());
        return bookAuthor;
    }

    @Override
    public boolean update(BookAuthor bookAuthor) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Integer integer) throws SQLException {
        return false;
    }

    @Override
    public List<BookAuthor> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public Optional<BookAuthor> search(Integer integer) throws SQLException {
        return Optional.empty();
    }
}
