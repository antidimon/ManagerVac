package antidimon.web.managervac.repositories;

import antidimon.web.managervac.models.entities.MyUser;
import antidimon.web.managervac.models.entities.Project;
import antidimon.web.managervac.models.entities.ProjectMember;
import antidimon.web.managervac.models.entities.ProjectMemberKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberKey> {

    List<ProjectMember> findByProject(Project project);

    List<ProjectMember> findByUser(MyUser user);
}
