package tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BorrowBookRecordsTM {
    private Integer id;
    private Integer book_Id;
    private String member_Id;
    private LocalDate borrowedDate;
    private LocalDate returnDate;
}
