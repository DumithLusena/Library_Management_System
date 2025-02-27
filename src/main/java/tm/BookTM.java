package tm;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookTM{
    private Integer id;
    private String name;
    private String isbn;
    private Double price;
}
