package tm;

import javafx.scene.control.CheckBox;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorTMWithCheckBox {
    private Integer id;
    private String name;
    private CheckBox checkBox;
}
