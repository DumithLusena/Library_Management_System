package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member {
    private String id;
    private String name;
    private String address;
    private String email;
    private String contact;

    public Member(Integer id, String e) {

    }
}
