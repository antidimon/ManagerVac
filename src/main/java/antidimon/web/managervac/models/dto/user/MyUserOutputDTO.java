package antidimon.web.managervac.models.dto.user;

import lombok.*;

import java.sql.Timestamp;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class MyUserOutputDTO {

    private long id;
    private String username;
    private String email;
    private String password;
    private Timestamp createdAt;
}
