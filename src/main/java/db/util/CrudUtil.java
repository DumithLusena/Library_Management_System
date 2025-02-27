package db.util;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {
    public static <T> T execute(String sql,Object... args) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement psTm = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            psTm.setObject(i + 1, args[i]);
        }
        if (sql.toLowerCase().startsWith("select")){
            ResultSet resultSet = psTm.executeQuery();
            return (T) resultSet;
        }

        Boolean b = psTm.executeUpdate() > 0;
        return (T) b;
    }
}