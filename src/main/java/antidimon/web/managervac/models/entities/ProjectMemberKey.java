package antidimon.web.managervac.models.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
public class ProjectMemberKey implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "user_id", nullable = false)
    private Long userId;


}
