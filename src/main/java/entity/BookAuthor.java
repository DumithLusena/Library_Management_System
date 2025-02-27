package entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookAuthor {
    private Integer bookId;
    private Integer authorId;
}
