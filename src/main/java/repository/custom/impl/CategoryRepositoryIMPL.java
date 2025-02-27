package repository.custom.impl;

import db.util.CrudUtil;
import entity.Category;
import repository.custom.CategoryRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryRepositoryIMPL implements CategoryRepository {
    @Override
    public boolean save(Category category) throws SQLException {
        String SQL = "INSERT INTO category(name) VALUES(?)";
        boolean execute = CrudUtil.execute(SQL, category.getName());
        return execute;
    }

    @Override
    public boolean update(Category category) throws SQLException {
        String SQL = "UPDATE category SET name = ? WHERE id = ?";
        return CrudUtil.execute(SQL,category.getName(),category.getId());
    }

    @Override
    public boolean delete(Integer integer) throws SQLException {
        String SQL = "DELETE FROM category WHERE id = ?";
        return CrudUtil.execute(SQL,integer);
    }

    @Override
    public List<Category> getAll() throws SQLException {
        String SQL = "SELECT * FROM category";
        ResultSet resultSet = CrudUtil.execute(SQL);
        List<Category> list = new ArrayList<>();
        while (resultSet.next()) {
            Category category = new Category();
            category.setId(resultSet.getInt(1));
            category.setName(resultSet.getString(2));
            list.add(category);
        }
        return list;
    }

    @Override
    public Optional<Category> search(Integer id) throws SQLException {
        String SQL = "SELECT * FROM category WHERE id = ?";
        ResultSet resultSet = CrudUtil.execute(SQL,id);
        if (resultSet.next()) {
            Category category = new Category();
            category.setId(resultSet.getInt(1));
            category.setName(resultSet.getString(2));
            return Optional.of(category);
        }
        return Optional.empty();
    }
}
