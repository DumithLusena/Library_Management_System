package repository.util;

import repository.CrudRepository;
import repository.custom.*;
import repository.custom.impl.*;

public class RepositoryFactory {
    private final static RepositoryFactory instance = new RepositoryFactory();

    private final AuthorRepository authorRepository;
    private final MemberRepository memberRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final AdminRepository adminRepository;
    private final BorrowBookRecordsRepository borrowBookRecordsRepository;
    private final FineRepository fineRepository;

    private RepositoryFactory(){
        authorRepository = new AuthorRepositoryIMPL();
        memberRepository = new MemberRepositoryIMPL();
        publisherRepository = new PublisherRepositoryIMPL();
        categoryRepository=new CategoryRepositoryIMPL();
        adminRepository=new AdminRepositoryIMPL();
        borrowBookRecordsRepository = new BorrowBookRecordsRepositoryIMPL();
        fineRepository = new FineRepositoryIMPL();
    }

    public <T extends CrudRepository>T getRepo(RepositoryTypes types){
        switch (types){
            case AUTHOR_REPOSITORY: return (T) this.authorRepository;
            case MEMBER_REPOSITORY: return (T) this.memberRepository;
            case PUBLISHER_REPOSITORY: return (T) this.publisherRepository;
            case CATEGORY_REPOSITORY: return (T) this.categoryRepository;
            case ADMIN_REPOSITORY: return (T) this.adminRepository;
            case BORROW_BOOK_RECORDS_REPOSITORY: return (T) this.borrowBookRecordsRepository;
            case FINE_REPOSITORY: return (T) this.fineRepository;
            default: return null;
        }
    }

    public static RepositoryFactory getInstance() {
        return instance;
    }
}
