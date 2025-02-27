package repository.custom.impl;

import db.DBConnection;
import entity.Member;
import repository.custom.MemberRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberRepositoryIMPL implements MemberRepository {

    @Override
    public boolean save(Member member) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement psTm = connection.prepareStatement("INSERT INTO member VALUES(?,?,?,?,?)");
        psTm.setString(1, member.getId());
        psTm.setString(2, member.getName());
        psTm.setString(3, member.getAddress());
        psTm.setString(4, member.getEmail());
        psTm.setString(5, member.getContact());
        return psTm.executeUpdate() > 0;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement psTm = connection.prepareStatement("DELETE FROM member WHERE id = ?");
        psTm.setString(1,id);
        return psTm.executeUpdate() > 0;
    }

    @Override
    public boolean update(Member member) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement psTm = connection.prepareStatement("UPDATE member set name = ?, address = ?, email = ?, contact = ? WHERE id = ?");
        psTm.setString(1,member.getName());
        psTm.setString(2,member.getAddress());
        psTm.setString(3,member.getEmail());
        psTm.setString(4,member.getContact());
        psTm.setString(5,member.getId());
        return psTm.executeUpdate() > 0;
    }

    @Override
    public Optional<Member> search(String memberId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement psTm = connection.prepareStatement("SELECT * FROM member WHERE id = ?");
        psTm.setString(1,memberId);
        ResultSet resultSet = psTm.executeQuery();
        if (resultSet.next()){
            Member obj = new Member(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
            return Optional.of(obj);
        }
        return Optional.empty();
    }

    @Override
    public List<Member> getAll() throws SQLException {
        ArrayList<Member> list = new ArrayList<>();

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement psTm = connection.prepareStatement("SELECT * FROM member");
        ResultSet resultSet = psTm.executeQuery();
        while(resultSet.next()){
            Member member = new Member(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
            list.add(member);
        }
        return list;
    }

}
