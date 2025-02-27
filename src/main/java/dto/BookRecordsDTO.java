package dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookRecordsDTO {
    private Integer id;
    private LocalDate borrowed_date;
    private Boolean isReturned;
    private LocalDate returnDate;
    private LocalDate returnedDate;
    private Integer book_id;
    private String member_id;
}
