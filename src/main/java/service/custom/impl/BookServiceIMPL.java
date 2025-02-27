package service.custom.impl;

import db.DBConnection;
import dto.BookDTO;
import entity.Book;
import entity.BookAuthor;
import entity.SubCategory;
import exceptions.custom.BookException;
import org.modelmapper.ModelMapper;
import repository.custom.BookAuthorRepository;
import repository.custom.BookRepository;
import repository.custom.SubCategoryRepository;
import service.custom.BookService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookServiceIMPL implements BookService {
    private final ModelMapper mapper;
    private final BookRepository bookRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final SubCategoryRepository subCategoryRepository;

    public BookServiceIMPL(ModelMapper mapper, BookRepository bookRepository, BookAuthorRepository bookAuthorRepository, SubCategoryRepository subCategoryRepository) {
        this.mapper = mapper;
        this.bookRepository = bookRepository;
        this.bookAuthorRepository = bookAuthorRepository;
        this.subCategoryRepository = subCategoryRepository;
    }

    @Override
    public boolean add(BookDTO bookDTO) throws BookException {
        Book book = this.convertDTOtoEntity(bookDTO);
        System.out.println(book);
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);

            Book save = bookRepository.save(book);

            List<BookAuthor> authorList = bookDTO.getAuthorIds().stream().map(e -> new BookAuthor(save.getId(), e)).collect(Collectors.toList());
            List<SubCategory> subCategoryList = bookDTO.getSubCategoryIds().stream().map(e -> new SubCategory(save.getId(),e)).collect(Collectors.toList());

            boolean flag = bookAuthorRepository.saveList(authorList);
            if (flag){
                flag = subCategoryRepository.saveList(subCategoryList);
                if (flag){
                    DBConnection.getInstance().getConnection().commit();
                    System.out.println("flage : "+flag);
                    return true;
                }
            }

            DBConnection.getInstance().getConnection().rollback();
            return false;

        } catch (SQLException e) {
            try {
                DBConnection.getInstance().getConnection().rollback();
            } catch (SQLException ex) {

            }
            if (((SQLException)e).getErrorCode() == 1062){
                throw new BookException("ID Already Exists - Cannot Save.", e);
            } else if (((SQLException)e).getErrorCode() == 1406) {
                String message = ((SQLException) e).getMessage();
                String[] er = message.split("'");
                throw new BookException("Data is To Large For "+er[1]);
            }
            throw new BookException("Error Occurred Contact Developer",e);
        }finally {
            try {
                System.out.println("service last : "+bookDTO);
                DBConnection.getInstance().getConnection().setAutoCommit(true);
            } catch (SQLException e) {

            }
        }
    }


    @Override
    public boolean update(BookDTO bookDTO) throws BookException {
        Book book = convertDTOtoEntity(bookDTO);
        List<BookAuthor> authorList = bookDTO.getAuthorIds().stream().map(e -> new BookAuthor(bookDTO.getId(), e)).collect(Collectors.toList());
        List<SubCategory> subCategoryList = bookDTO.getSubCategoryIds().stream().map(e -> new SubCategory(bookDTO.getId(),e)).collect(Collectors.toList());
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            boolean update = bookRepository.update(book);
            if (update){
                boolean isDeletedBookAuthor = bookAuthorRepository.deleteAllWithBookId(bookDTO.getId());
                if (isDeletedBookAuthor){
                    boolean isAllSubCategoriesDeleted = subCategoryRepository.deleteAllWithBookId(bookDTO.getId());
                    if (isAllSubCategoriesDeleted){
                        boolean isAllBookAuthorRecordAdded = bookAuthorRepository.saveList(authorList);
                        if (isAllBookAuthorRecordAdded){
                            boolean isAllSubCategoryRecordAdded = subCategoryRepository.saveList(subCategoryList);
                            if (isAllSubCategoryRecordAdded){
                                DBConnection.getInstance().getConnection().commit();
                                return true;
                            }
                        }
                    }
                }
            }
            DBConnection.getInstance().getConnection().rollback();
            return false;

        } catch (SQLException e) {
            try {
                DBConnection.getInstance().getConnection().rollback();
            } catch (SQLException ex) {

            }
            if (((SQLException)e).getErrorCode() == 1406) {
                String message = ((SQLException) e).getMessage();
                String[] er = message.split("'");
                throw new BookException("Data is To Large For "+er[1]);
            }
            throw new BookException("Error Occurred Contact Developer",e);
        }finally {
            try {
                DBConnection.getInstance().getConnection().setAutoCommit(true);
            } catch (SQLException e) {

            }
        }
    }

    @Override
    public boolean delete(Integer id) throws BookException {
        try {
            return bookRepository.delete(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BookException("Not Implemented Yet Fully",e);
        }
    }

    @Override
    public Optional<BookDTO> search(Integer id) throws BookException {
        try {
            Optional<Book> search = bookRepository.search(id);
            if (search.isPresent()){
                Book book = search.get();
                List<Integer> authorIds = bookAuthorRepository.getAll(id).stream().map(e -> e.getAuthorId()).collect(Collectors.toList());
                List<Integer> subCategoryIds = subCategoryRepository.getAll(id).stream().map(e -> e.getCategoryId()).collect(Collectors.toList());
                BookDTO bookDTO = convertEntityToDTO(book);
                bookDTO.setAuthorIds(authorIds);
                bookDTO.setSubCategoryIds(subCategoryIds);
                return Optional.of(bookDTO);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new BookException("Contact Developer",e);
        }
    }

    @Override
    public List<BookDTO> getAll() throws BookException {
        try {
            List<Book> all = bookRepository.getAll();
            if (all.isEmpty()){
                throw new BookException("No Books Found");
            }
            List<BookDTO> bookDTOS = new ArrayList<>();
            for (Book book : all) {
                BookDTO bookDTO = convertEntityToDTO(book);
                bookDTOS.add(bookDTO);
            }
            return bookDTOS;
        } catch (SQLException e) {
            throw new BookException("Contact Developer",e);
        }
    }

    private Book convertDTOtoEntity(BookDTO bookDTO){
        System.out.println("Book Record :"+bookDTO);
        return mapper.map(bookDTO,Book.class);
    }

    private BookDTO convertEntityToDTO(Book bookEntity){
        return mapper.map(bookEntity,BookDTO.class);
    }
}
