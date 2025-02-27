package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FineDTO {
    private Integer id;
    private BigDecimal amount;
    private Short date_count;
    private Integer book_record_id;

    public FineDTO(Integer recordId, Integer bookId, String memberId, int daysLate, int fineAmount, int paymentAmount, int balance) {}
}
