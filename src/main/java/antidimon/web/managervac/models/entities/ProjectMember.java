package antidimon.web.managervac.models.entities;

import antidimon.web.managervac.models.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "project_members")
@Getter
@Setter
@NoArgsConstructor
public class ProjectMember implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ProjectMemberKey id;

    @ManyToOne
    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private MyUser user;

    @Enumerated(EnumType.STRING)
    private Role role;

}
