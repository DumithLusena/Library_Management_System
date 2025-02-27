package repository.custom.impl;

import db.util.CrudUtil;
import entity.SubCategory;
import repository.custom.SubCategoryRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubCategoryRepositoryIMPL implements SubCategoryRepository {

    boolean flag = true;
    @Override
    public boolean saveList(List<SubCategory> list) throws SQLException {
        flag = true;
        for (SubCategory subCategory : list) {
            if (flag){
                save(subCategory);
            }else{
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean deleteAllWithBookId(Integer bookId) throws SQLException {
        String SQL = "DELETE FROM sub_categories WHERE book_id = ?";
        return CrudUtil.execute(SQL, bookId);
    }

    @Override
    public List<SubCategory> getAll(Integer bookId) throws SQLException {
        String SQL = "SELECT * FROM sub_categories WHERE book_id = ?";
        ResultSet resultSet = CrudUtil.execute(SQL, bookId);
        List<SubCategory> list = new ArrayList<>();
        while (resultSet.next()){
            SubCategory subCategory = new SubCategory();
            subCategory.setBookId(resultSet.getInt(1));
            subCategory.setCategoryId(resultSet.getInt(2));
            list.add(subCategory);
        }
        return list;
    }

    @Override
    public SubCategory save(SubCategory subCategory) throws SQLException {
        String SQL="INSERT INTO sub_categories(book_id,category_id) VALUES(?,?)";
        flag = CrudUtil.execute(SQL, subCategory.getBookId(), subCategory.getCategoryId());
        return subCategory;
    }

    @Override
    public boolean update(SubCategory subCategory) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Integer integer) throws SQLException {
        return false;
    }

    @Override
    public List<SubCategory> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public Optional<SubCategory> search(Integer integer) throws SQLException {
        return Optional.empty();
    }
}
