package repository.custom;

import entity.BookRecords;
import repository.BookCrudRepo;
import repository.CrudRepository;

import java.util.List;

public interface BorrowBookRecordsRepository extends CrudRepository<BookRecords, Integer> {

    void markAsReturned(Integer recordId);
    
    List<BookRecords> searchByBookId(String searchKeyWord);

    List<BookRecords> searchByMemberId(String searchKeyWord);
}
