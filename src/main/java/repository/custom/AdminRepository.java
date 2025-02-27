package repository.custom;

import dto.AdminDTO;
import entity.Admin;
import repository.CrudRepository;

import java.sql.SQLException;

public interface AdminRepository extends CrudRepository<Admin, Integer> {
    AdminDTO login(String email, String password) throws SQLException;
}
