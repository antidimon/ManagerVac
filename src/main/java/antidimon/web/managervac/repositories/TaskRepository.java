package antidimon.web.managervac.repositories;

import antidimon.web.managervac.models.entities.Project;
import antidimon.web.managervac.models.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject(Project project);

}
