package service.util;

import repository.util.BookRepoFactory;
import repository.util.BookRepoTypes;
import service.SuperService;
import service.custom.BookService;
import service.custom.impl.BookServiceIMPL;

public class BookServiceFactory {
    static final BookRepoFactory bookRepoFactory = BookRepoFactory.getInstance();
    private static final OtherDependancies otherDependancies = OtherDependancies.getInstance();
    private static final BookServiceFactory getInstance = new BookServiceFactory();

    private final BookService bookService;

    private BookServiceFactory(){
        bookService = new BookServiceIMPL(otherDependancies.getModelMapper()
                ,bookRepoFactory.getBookRepo(BookRepoTypes.BOOK_REPOSITORY)
                ,bookRepoFactory.getBookRepo(BookRepoTypes.BOOK_AUTHOR_REPOSITORY)
                ,bookRepoFactory.getBookRepo(BookRepoTypes.SUB_CATEGORY_REPOSITORY));
    }

    public static BookServiceFactory getInstance(){
        return getInstance;
    }

    public SuperService getBookService(BookServiceTypes types){
        switch (types){
            case BOOK_SERVICE: return bookService;
            default:return null;
        }
    }

}
