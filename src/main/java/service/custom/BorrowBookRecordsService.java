package service.custom;

import dto.BookRecordsDTO;
import service.CrudService;
import java.util.List;

public interface BorrowBookRecordsService extends CrudService<BookRecordsDTO,Integer> {

    void markAsReturned(Integer recordId);

    List<BookRecordsDTO> searchRecords(String searchKeyWord, boolean searchByBookId);
}
