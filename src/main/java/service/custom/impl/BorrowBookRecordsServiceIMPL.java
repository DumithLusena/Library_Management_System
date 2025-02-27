package service.custom.impl;

import db.DBConnection;
import dto.BookRecordsDTO;
import entity.*;
import exceptions.custom.BorrowBookRecordsException;
import org.modelmapper.ModelMapper;
import repository.custom.BorrowBookRecordsRepository;
import repository.custom.BookRepository;
import repository.custom.MemberRepository;
import service.custom.BorrowBookRecordsService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BorrowBookRecordsServiceIMPL implements BorrowBookRecordsService {

    private final ModelMapper mapper;
    private final BorrowBookRecordsRepository borrowBookRecordsRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public BorrowBookRecordsServiceIMPL(ModelMapper mapper, BorrowBookRecordsRepository bookRecordsRepository,BookRepository bookRepository,MemberRepository memberRepository) {
        this.mapper = mapper;
        this.borrowBookRecordsRepository = bookRecordsRepository;
        this.bookRepository  = bookRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean add(BookRecordsDTO bookRecordsDTO) throws BorrowBookRecordsException {
        BookRecords bookRecords = this.convertDTOtoEntity(bookRecordsDTO);
        bookRecordsDTO.setReturnedDate(bookRecordsDTO.getReturnDate());
        try {
            validateDates(bookRecordsDTO);
            return borrowBookRecordsRepository.save(bookRecords);
        } catch (SQLException e) {
            handleSQLException(e);
        }
        throw new BorrowBookRecordsException("Error Occurred Contact Developer");
    }

    private void validateDates(BookRecordsDTO dto) throws BorrowBookRecordsException {
        if (dto.getBorrowed_date() == null){
            throw new BorrowBookRecordsException("Borrowed Date Cannot Be Null");
        }

        if (dto.getBorrowed_date().isAfter(LocalDate.now())){
            throw new BorrowBookRecordsException("Borrowed Date Cannot Be Future Date");
        }

        if (dto.getReturnDate().isBefore(dto.getBorrowed_date())){
            throw new BorrowBookRecordsException("Return Date Cannot Be Before Borrowed Date");
        }
    }

    private void handleSQLException(SQLException e) throws BorrowBookRecordsException {
        try {
            DBConnection.getInstance().getConnection().rollback();
        } catch (SQLException ex) {
            throw new BorrowBookRecordsException("Error rolling back transaction", ex);
        }

        if (e.getErrorCode() == 1406) {
            String message = e.getMessage();
            String[] er = message.split("'");
            throw new BorrowBookRecordsException("Data is Too Large For " + er[1]);
        }
        throw new BorrowBookRecordsException("Error Occurred Contact Developer", e);
    }


    @Override
    public boolean update(BookRecordsDTO bookRecordsDTO) throws BorrowBookRecordsException {
        return false;
    }

    @Override
    public boolean delete(Integer integer) throws BorrowBookRecordsException {
        return false;
    }

    @Override
    public Optional<BookRecordsDTO> search(Integer integer) throws BorrowBookRecordsException {
        try {
            Optional<BookRecords> search = borrowBookRecordsRepository.search(integer);
            if (search.isPresent()){
                BookRecords bookRecords = search.get();
                BookRecordsDTO bookRecordsDTO = convertEntityToDTO(bookRecords);
                return Optional.of(bookRecordsDTO);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BookRecordsDTO> getAll() throws BorrowBookRecordsException {
        try {
            List<BookRecords> all = borrowBookRecordsRepository.getAll();
            if (all.isEmpty()){
                throw new BorrowBookRecordsException("No Borrow Book Records");
            }

            List<BookRecordsDTO> list = new ArrayList<>();
            for (BookRecords bookRecords : all){
                BookRecordsDTO bookRecordsDTO = convertEntityToDTO(bookRecords);
                list.add(bookRecordsDTO);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private BookRecords convertDTOtoEntity(BookRecordsDTO bookRecordsDTO){
        System.out.println("BookRecordsDTO CONVERT: " + bookRecordsDTO);
        return mapper.map(bookRecordsDTO, BookRecords.class);
    }


        private BookRecordsDTO convertEntityToDTO(BookRecords bookRecords){
        return mapper.map(bookRecords,BookRecordsDTO.class);
    }

    @Override
    public void markAsReturned(Integer recordId) {
        borrowBookRecordsRepository.markAsReturned(recordId);
    }


    @Override
    public List<BookRecordsDTO> searchRecords(String searchKeyWord, boolean searchByBookId) {
        List<BookRecords> bookRecords;

        if (searchByBookId) {
            bookRecords = borrowBookRecordsRepository.searchByBookId(searchKeyWord);
        } else {
            bookRecords = borrowBookRecordsRepository.searchByMemberId(searchKeyWord);
        }

        return bookRecords.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private BookRecordsDTO convertToDTO(BookRecords bookRecord) {
        return new BookRecordsDTO(
                bookRecord.getId(),
                bookRecord.getBorrowed_date(),
                bookRecord.getIsReturned(),
                bookRecord.getReturnDate(),
                bookRecord.getReturnedDate(),
                bookRecord.getBook_id(),
                bookRecord.getMember_id()
        );
    }
}
