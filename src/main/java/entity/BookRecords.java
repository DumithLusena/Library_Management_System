package entity;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookRecords {
    private Integer id;
    private LocalDate borrowed_date;
    private Boolean isReturned;
    private LocalDate returnDate;
    private LocalDate returnedDate;
    private Integer book_id;
    private String member_id;

    public boolean isReturned() {
        return this.returnedDate != null;
    }
}
