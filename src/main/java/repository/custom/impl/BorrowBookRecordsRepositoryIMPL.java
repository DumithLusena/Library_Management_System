package repository.custom.impl;

import db.DBConnection;
import db.util.CrudUtil;
import entity.BookRecords;
import javafx.scene.control.Alert;
import repository.custom.BorrowBookRecordsRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BorrowBookRecordsRepositoryIMPL implements BorrowBookRecordsRepository {

@Override
public boolean save(BookRecords bookRecords) throws SQLException {
    String SQL = "INSERT INTO book_records VALUES(?,?,?,?,?,?,?)";
    try (PreparedStatement psTm = DBConnection.getInstance().getConnection()
            .prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

        psTm.setInt(1, bookRecords.getId());
        psTm.setDate(2, Date.valueOf(bookRecords.getBorrowed_date()));
        psTm.setBoolean(3, bookRecords.isReturned());
        psTm.setDate(4, Date.valueOf(bookRecords.getReturnDate()));


        if (bookRecords.getReturnedDate() == null){
            psTm.setDate(5, null);
        }

        psTm.setInt(6, bookRecords.getBook_id());
        psTm.setString(7, bookRecords.getMember_id());

        int affectedRows = psTm.executeUpdate();
        if (affectedRows > 0) {
            try (ResultSet generatedKeys = psTm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    bookRecords.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        }
    }
    return false;
}

    @Override
    public boolean update(BookRecords bookRecords) throws SQLException {
        String SQL = "UPDATE book_records SET borrowed_date = ?, isReturned = ?, return_date = ?, returned_date = ?, book_id = ?, member_id = ? WHERE id = ?";
        boolean execute = CrudUtil.execute(SQL, bookRecords.getBorrowed_date(), bookRecords.isReturned(), bookRecords.getReturnDate(), bookRecords.getReturnedDate(), bookRecords.getBook_id(), bookRecords.getMember_id(), bookRecords.getId());
        return execute;
    }

    @Override
    public boolean delete(Integer integer) throws SQLException {
        String SQL = "DELETE FROM book_records WHERE id = ?";
        boolean execute = CrudUtil.execute(SQL, integer);
        return execute;
    }

    @Override
    public List<BookRecords> getAll() throws SQLException {
        String SQL = "SELECT * FROM book_records";
        List<BookRecords> list = new ArrayList<>();

        try (ResultSet resultSet = CrudUtil.execute(SQL)) {
            while (resultSet.next()) {
                BookRecords bookRecords = new BookRecords();
                bookRecords.setId(resultSet.getInt(1));
                bookRecords.setBorrowed_date(resultSet.getDate(2).toLocalDate());
                bookRecords.setIsReturned(resultSet.getBoolean(3));
                bookRecords.setReturnDate(resultSet.getDate(4).toLocalDate());

                Date returnedDate = resultSet.getDate(5);
                if (returnedDate != null) {
                    bookRecords.setReturnedDate(returnedDate.toLocalDate());
                }

                bookRecords.setBook_id(resultSet.getInt(6));
                bookRecords.setMember_id(resultSet.getString(7));
                list.add(bookRecords);
            }
        }
        return list;
    }


    @Override
    public Optional<BookRecords> search(Integer integer) throws SQLException {
        String SQL = "SELECT * FROM book_records WHERE id = ?";
        ResultSet resultSet = CrudUtil.execute(SQL, integer);
        if (resultSet.next()){
            BookRecords bookRecords = new BookRecords();
            bookRecords.setId(resultSet.getInt(1));
            bookRecords.setBorrowed_date(resultSet.getDate(2).toLocalDate());
            bookRecords.setIsReturned(resultSet.getBoolean(3));
            bookRecords.setReturnDate(resultSet.getDate(4).toLocalDate());
            bookRecords.setReturnedDate(resultSet.getDate(5).toLocalDate());
            bookRecords.setBook_id(resultSet.getInt(6));
            bookRecords.setMember_id(resultSet.getString(7));
            return Optional.of(bookRecords);
        }
        return Optional.empty();
    }

    @Override
    public void markAsReturned(Integer recordId) {
        try {
            String SQL = "UPDATE book_records SET isReturned = ? WHERE id = ?";
            boolean execute = CrudUtil.execute(SQL, true, recordId);
            if (!execute){
                new Alert(Alert.AlertType.ERROR,"Failed to mark as returned").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Failed to mark as returned").show();
        }
    }

    @Override
    public List<BookRecords> searchByBookId(String searchKeyWord) {
        String SQL = "SELECT * FROM book_records WHERE book_id LIKE ?";
        List<BookRecords> list = new ArrayList<>();

        try (ResultSet resultSet = CrudUtil.execute(SQL, "%" + searchKeyWord + "%")) {
            while (resultSet.next()) {
                BookRecords bookRecords = new BookRecords();
                bookRecords.setId(resultSet.getInt(1));
                bookRecords.setBorrowed_date(resultSet.getDate(2).toLocalDate());
                bookRecords.setIsReturned(resultSet.getBoolean(3));
                bookRecords.setReturnDate(resultSet.getDate(4).toLocalDate());

                Date returnedDate = resultSet.getDate(5);
                if (returnedDate != null) {
                    bookRecords.setReturnedDate(returnedDate.toLocalDate());
                }

                bookRecords.setBook_id(resultSet.getInt(6));
                bookRecords.setMember_id(resultSet.getString(7));
                list.add(bookRecords);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to search by book id").showAndWait();
        }
        return list;
    }

    @Override
    public List<BookRecords> searchByMemberId(String searchKeyWord) {
        String SQL = "SELECT * FROM book_records WHERE member_id LIKE ?";
        List<BookRecords> list = new ArrayList<>();

        try (ResultSet resultSet = CrudUtil.execute(SQL, "%" + searchKeyWord + "%")) {
            while (resultSet.next()) {
                BookRecords bookRecords = new BookRecords();
                bookRecords.setId(resultSet.getInt(1));
                bookRecords.setBorrowed_date(resultSet.getDate(2).toLocalDate());
                bookRecords.setIsReturned(resultSet.getBoolean(3));
                bookRecords.setReturnDate(resultSet.getDate(4).toLocalDate());

                Date returnedDate = resultSet.getDate(5);
                if (returnedDate != null) {
                    bookRecords.setReturnedDate(returnedDate.toLocalDate());
                }

                bookRecords.setBook_id(resultSet.getInt(6));
                bookRecords.setMember_id(resultSet.getString(7));
                list.add(bookRecords);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to search by member id").showAndWait();
        }
        return list;
    }
}
