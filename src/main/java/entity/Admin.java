package entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Admin {
    private String username;
    private String email;
    private String password;

}
