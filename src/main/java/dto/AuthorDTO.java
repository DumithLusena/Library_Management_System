package dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AuthorDTO {
    private Integer id;
    private String name;
    private String contact;
}
