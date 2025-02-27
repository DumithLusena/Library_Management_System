package repository.util;

import repository.BookCrudRepo;
import repository.custom.BookAuthorRepository;
import repository.custom.BookRepository;
import repository.custom.SubCategoryRepository;
import repository.custom.impl.BookAuthorRepositoryIMPL;
import repository.custom.impl.BookRepositoryIMPL;
import repository.custom.impl.SubCategoryRepositoryIMPL;

public class BookRepoFactory {
    private final static BookRepoFactory instance = new BookRepoFactory();

    private final BookRepository bookRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final SubCategoryRepository subCategoryRepository;

    private BookRepoFactory(){
        bookRepository = new BookRepositoryIMPL();
        bookAuthorRepository = new BookAuthorRepositoryIMPL();
        subCategoryRepository = new SubCategoryRepositoryIMPL();
    }

    public <T extends BookCrudRepo>T getBookRepo(BookRepoTypes types){
        switch (types){
            case BOOK_REPOSITORY: return (T) this.bookRepository;
            case BOOK_AUTHOR_REPOSITORY: return (T) this.bookAuthorRepository;
            case SUB_CATEGORY_REPOSITORY: return (T) this.subCategoryRepository;
            default:return null;
        }
    }

    public static BookRepoFactory getInstance(){
        return instance;
    }
}
