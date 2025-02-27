package repository.custom.impl;

import db.util.CrudUtil;
import entity.Publisher;
import repository.custom.PublisherRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PublisherRepositoryIMPL implements PublisherRepository {

    @Override
    public boolean save(Publisher publisher) throws SQLException {
        String SQL = "INSERT INTO publisher(name,location,contact) VALUES(?,?,?)";
        boolean execute = CrudUtil.execute(SQL, publisher.getName(), publisher.getLocation(), publisher.getContact());
        return execute;
    }

    @Override
    public boolean update(Publisher publisher) throws SQLException {
        String SQL = "UPDATE publisher SET name = ?, location = ?, contact = ? WHERE id = ?";
        return CrudUtil.execute(SQL,publisher.getName(),publisher.getLocation(),publisher.getContact(),publisher.getId());
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String SQL = "DELETE FROM publisher WHERE id = ?";
        return CrudUtil.execute(SQL,id);
    }

    @Override
    public List<Publisher> getAll() throws SQLException {
        String SQL = "SELECT * FROM publisher";
        ResultSet resultSet = CrudUtil.execute(SQL);
        List<Publisher> list = new ArrayList<>();
        while (resultSet.next()){
            Publisher publisher = new Publisher(resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );
            list.add(publisher);
        }
        return list;
    }

    @Override
    public Optional<Publisher> search(Integer id) throws SQLException {
        String SQL = "SELECT * FROM publisher WHERE id = ?";
        ResultSet resultSet = CrudUtil.execute(SQL,id);
        if (resultSet.next()){
            Publisher publisher = new Publisher();
            publisher.setId(resultSet.getInt(1));
            publisher.setName(resultSet.getString(2));
            publisher.setLocation(resultSet.getString(3));
            publisher.setContact(resultSet.getString(4));
            return Optional.of(publisher);
        }
        return Optional.empty();
    }
}
