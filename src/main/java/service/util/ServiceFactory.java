package service.util;

import repository.util.BookRepoTypes;
import repository.util.RepositoryFactory;
import repository.util.RepositoryTypes;
import service.SuperService;
import service.custom.*;
import service.custom.impl.*;

import static service.util.BookServiceFactory.bookRepoFactory;

public class ServiceFactory {
    private static final RepositoryFactory repositoryFactory = RepositoryFactory.getInstance();
    private static final OtherDependancies otherDependancies = OtherDependancies.getInstance();
    private static final ServiceFactory getInstance = new ServiceFactory();

    private final MemberService memberService;
    private final PublisherService publisherService;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final AdminService adminService;
    private final BorrowBookRecordsService borrowBookRecordsService;
    private final FineService fineService;


    private ServiceFactory() {
        memberService = new MemberServiceIMPL(otherDependancies.getModelMapper(),repositoryFactory.getRepo(RepositoryTypes.MEMBER_REPOSITORY));
        publisherService = new PublisherServiceIMPL(otherDependancies.getModelMapper(),repositoryFactory.getRepo(RepositoryTypes.PUBLISHER_REPOSITORY));
        authorService  = new AuthorServiceIMPL(otherDependancies.getModelMapper(),repositoryFactory.getRepo(RepositoryTypes.AUTHOR_REPOSITORY));
        categoryService = new CategoryServiceIMPL(otherDependancies.getModelMapper(),repositoryFactory.getRepo(RepositoryTypes.CATEGORY_REPOSITORY));
        adminService = new AdminServiceIMPL(otherDependancies.getModelMapper(),repositoryFactory.getRepo(RepositoryTypes.ADMIN_REPOSITORY));
        borrowBookRecordsService = new BorrowBookRecordsServiceIMPL(otherDependancies.getModelMapper(),repositoryFactory.getRepo(RepositoryTypes.BORROW_BOOK_RECORDS_REPOSITORY)
                ,bookRepoFactory.getBookRepo(BookRepoTypes.BOOK_REPOSITORY),repositoryFactory.getRepo(RepositoryTypes.MEMBER_REPOSITORY));

        fineService = new FineServiceIMPL(otherDependancies.getModelMapper(),repositoryFactory.getRepo(RepositoryTypes.FINE_REPOSITORY),repositoryFactory.getRepo(RepositoryTypes.BORROW_BOOK_RECORDS_REPOSITORY));
    }

    public static ServiceFactory getInstance() {
        return getInstance;
    }

    public SuperService getService(ServiceTypes types){
        switch (types){
            case MEMBER_SERVICE: return memberService;
            case PUBLISHER_SERVICE: return publisherService;
            case AUTHOR_SERVICE: return authorService;
            case CATEGORY_SERVICE: return categoryService;
            case ADMIN_SERVICE: return adminService;
            case BORROW_BOOK_RECORDS_SERVICE: return borrowBookRecordsService;
            case FINE_SERVICE: return fineService;
            default:return null;
        }
    }
}
