package repository.custom.impl;

import db.util.CrudUtil;
import entity.Author;
import repository.custom.AuthorRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorRepositoryIMPL implements AuthorRepository {

    @Override
    public boolean save(Author author) throws SQLException {
        String SQL = "INSERT INTO author(name,contact) VALUES(?,?)";
        boolean execute = CrudUtil.execute(SQL, author.getName(), author.getContact());
        return execute;
    }

    @Override
    public boolean update(Author author) throws SQLException {
        String SQL = "UPDATE author SET name = ?, contact = ? WHERE id = ?";
        boolean execute = CrudUtil.execute(SQL, author.getName(),author.getContact(),author.getId());
        return execute;
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String SQL = "DELETE FROM author WHERE id = ?";
        return CrudUtil.execute(SQL,id);
    }

    @Override
    public List<Author> getAll() throws SQLException {
        String SQL = "SELECT * FROM author";
        ResultSet resultSet = CrudUtil.execute(SQL);
        List<Author> list = new ArrayList<>();
        while (resultSet.next()){
            Author author = new Author();
            author.setId(resultSet.getInt(1));
            author.setName(resultSet.getString(2));
            author.setContact(resultSet.getString(3));
            list.add(author);
        }
        return list;
    }

    @Override
    public Optional<Author> search(Integer id) throws SQLException {
        String SQL = "SELECT * FROM author WHERE id = ?";
        ResultSet resultSet = CrudUtil.execute(SQL,id);
        if (resultSet.next()){
            Author author = new Author();
            author.setId(resultSet.getInt(1));
            author.setName(resultSet.getString(2));
            author.setContact(resultSet.getString(3));
            return Optional.of(author);
        }
        return Optional.empty();
    }
}
