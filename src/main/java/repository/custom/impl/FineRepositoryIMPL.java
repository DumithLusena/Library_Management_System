package repository.custom.impl;

import db.util.CrudUtil;
import entity.Fine;
import repository.custom.FineRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FineRepositoryIMPL implements FineRepository {

    @Override
    public boolean save(Fine fine) throws SQLException {
        String SQL = "INSERT INTO fine(amount, date_count, book_record_id) VALUES(?, ?, ?)";
        return CrudUtil.execute(SQL, fine.getAmount(), fine.getDate_count(), fine.getBook_record_id());
    }

    @Override
    public boolean update(Fine fine) throws SQLException {
        String SQL = "UPDATE fine SET amount = ?, date_count = ?, book_record_id = ? WHERE id = ?";
        return CrudUtil.execute(SQL, fine.getAmount(), fine.getDate_count(), fine.getBook_record_id(), fine.getId());
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String SQL = "DELETE FROM fine WHERE id = ?";
        return CrudUtil.execute(SQL, id);
    }

    @Override
    public List<Fine> getAll() throws SQLException {
        String SQL = "SELECT * FROM fine";
        List<Fine> fineList = new ArrayList<>();

        try (ResultSet resultSet = CrudUtil.execute(SQL)) {
            while (resultSet.next()) {
                fineList.add(mapToFine(resultSet));
            }
        }
        return fineList;
    }

    @Override
    public Optional<Fine> search(Integer id) throws SQLException {
        String SQL = "SELECT * FROM fine WHERE id = ?";

        try (ResultSet resultSet = CrudUtil.execute(SQL, id)) {
            if (resultSet.next()) {
                return Optional.of(mapToFine(resultSet));
            }
        }
        return Optional.empty();
    }

    private Fine mapToFine(ResultSet resultSet) throws SQLException {
        Fine fine = new Fine();
        fine.setId(resultSet.getInt("id"));
        fine.setAmount(resultSet.getBigDecimal("amount"));
        fine.setDate_count(resultSet.getShort("date_count"));
        fine.setBook_record_id(resultSet.getInt("book_record_id"));
        return fine;
    }
}
