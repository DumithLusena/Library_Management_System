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
public class FineTM {
    private Integer recordId;
    private Integer bookId;
    private String memberId;
    private LocalDate borrowedDate;
    private LocalDate returnDate;
}
