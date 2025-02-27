package repository.custom.impl;

import db.DBConnection;
import db.util.CrudUtil;
import dto.AdminDTO;
import entity.Admin;
import repository.custom.AdminRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AdminRepositoryIMPL implements AdminRepository {

    @Override
    public boolean save(Admin admin) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String SQL = "INSERT INTO admin(username,email,password) VALUES(?,?,?)";
        PreparedStatement psTm = connection.prepareStatement(SQL);
        psTm.setString(1, admin.getUsername());
        psTm.setString(2, admin.getEmail());
        psTm.setString(3, admin.getPassword());
        return psTm.executeUpdate() > 0;
    }

    @Override
    public boolean update(Admin admin) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Integer integer) throws SQLException {
        return false;
    }

    @Override
    public List<Admin> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public Optional<Admin> search(Integer integer) throws SQLException {
        String SQL = "SELECT * FROM admin WHERE email = ?";
        ResultSet resultSet = CrudUtil.execute(SQL, integer);
        if (resultSet.next()) {
            Admin admin = new Admin();
            admin.setUsername(resultSet.getString(1));
            admin.setEmail(resultSet.getString(2));
            admin.setPassword(resultSet.getString(3));
            return Optional.of(admin);
        }
        return Optional.empty();
    }

    @Override
    public AdminDTO login(String email, String password) throws SQLException {
        String SQL = "SELECT * FROM admin WHERE email = ? AND password = ?";
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement pst = connection.prepareStatement(SQL)) {
            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                AdminDTO adminDTO = new AdminDTO();
                adminDTO.setEmail(resultSet.getString("email"));
                adminDTO.setPassword(resultSet.getString("password"));
                adminDTO.setUserName(resultSet.getString("username"));
                return adminDTO;
            }
        } catch (SQLException e) {
            throw new SQLException("Error executing login query", e);
        }
        return null;
    }
}
