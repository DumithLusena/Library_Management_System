package repository.custom;

import entity.SubCategory;
import repository.BookCrudRepo;
import repository.CrudRepository;

import java.sql.SQLException;
import java.util.List;

public interface SubCategoryRepository extends BookCrudRepo<SubCategory, Integer> {
    public boolean saveList(List<SubCategory> list) throws SQLException;
    public boolean deleteAllWithBookId(Integer bookId) throws SQLException;
    public List<SubCategory> getAll(Integer bookId) throws SQLException;
}
