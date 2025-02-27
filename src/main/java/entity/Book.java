package entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book {
    private Integer id;
    private String name;
    private String isbn;
    private Double price;
    private Integer publisherId;
    private Integer mainCategoryId;
}
