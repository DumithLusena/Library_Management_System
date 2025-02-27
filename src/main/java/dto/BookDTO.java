package dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BookDTO {
    private Integer id;
    private String name;
    private String isbn;
    private Double price;
    private Integer publisherId;
    private Integer mainCategoryId;
    private List<Integer> subCategoryIds;
    private List<Integer> authorIds;
}
