package repository.custom.impl;

import db.DBConnection;
import db.util.CrudUtil;
import entity.Book;
import repository.custom.BookRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryIMPL implements BookRepository {
    @Override
    public Book save(Book book) throws SQLException {
        String SQL = "INSERT INTO book VALUES(?,?,?,?,?,?)";
        //boolean execute = CrudUtil.execute(SQL, book.getId(),book.getName(),book.getIsbn(),book.getPrice(),book.getPublisherId(),book.getMainCategoryId());
        PreparedStatement psTm = DBConnection.getInstance().getConnection().prepareStatement(SQL,PreparedStatement.RETURN_GENERATED_KEYS);
        psTm.setInt(1, book.getId());
        psTm.setString(2, book.getName());
        psTm.setString(3, book.getIsbn());
        psTm.setDouble(4, book.getPrice());
        psTm.setInt(5, book.getPublisherId());
        psTm.setInt(6, book.getMainCategoryId());
        int affectedRows = psTm.executeUpdate();
        if (affectedRows > 0) {
            ResultSet generatedKeys = psTm.getGeneratedKeys();
            if (generatedKeys.next()) {
                book.setId(generatedKeys.getInt(1));
            }
        }
        return book;
    }

    @Override
    public boolean update(Book book) throws SQLException {
        String SQL = "UPDATE book SET name = ?, isbn = ?, price = ?, publisher_Id = ?, main_Category_Id = ? WHERE id = ?";
        boolean execute = CrudUtil.execute(SQL, book.getName(), book.getIsbn(), book.getPrice(), book.getPublisherId(), book.getMainCategoryId(), book.getId());
        return execute;
    }

    @Override
    public boolean delete(Integer integer) throws SQLException {
        String SQL = "DELETE FROM book WHERE id = ?";
        boolean execute = CrudUtil.execute(SQL,integer);
        return execute;
    }

    @Override
    public List<Book> getAll() throws SQLException {
        String SQL = "SELECT * FROM book";
        ResultSet resultSet = CrudUtil.execute(SQL);
        List<Book> list = new ArrayList<>();
        while (resultSet.next()){
            Book book = new Book();
            book.setId(resultSet.getInt(1));
            book.setName(resultSet.getString(2));
            book.setIsbn(resultSet.getString(3));
            book.setPrice(resultSet.getDouble(4));
            book.setPublisherId(resultSet.getInt(5));
            book.setMainCategoryId(resultSet.getInt(6));
            list.add(book);
        }
        return list;
    }

    @Override
    public Optional<Book> search(Integer integer) throws SQLException {
       String SQL = "SELECT * FROM book WHERE id = ?";
       ResultSet resultSet = CrudUtil.execute(SQL,integer);
        if (resultSet.next()){
            Book book = new Book();
            book.setId(resultSet.getInt(1));
            book.setName(resultSet.getString(2));
            book.setIsbn(resultSet.getString(3));
            book.setPrice(resultSet.getDouble(4));
            book.setPublisherId(resultSet.getInt(5));
            book.setMainCategoryId(resultSet.getInt(6));
            return Optional.of(book);
        }
        return Optional.empty();
    }

}
