package antidimon.web.managervac.repositories;

import antidimon.web.managervac.models.entities.Project;
import antidimon.web.managervac.models.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject(Project project);

    @Query(value = "SELECT t.* FROM tasks t INNER JOIN task_developers td ON t.id = td.task_id " +
            "WHERE td.user_id = :userId",
            countQuery = "SELECT count(t.id) FROM tasks t INNER JOIN task_developers td ON t.id = td.task_id " +
                    "WHERE td.user_id = :userId",
            nativeQuery = true)
    Page<Task> findByUserId(@Param("userId") long userId, Pageable pageable);

    @Query(value = "SELECT t.* FROM tasks t INNER JOIN task_developers td ON t.id = td.task_id " +
            "WHERE td.user_id = :userId AND t.project_id = :projectId",
            countQuery = "SELECT count(t.id) FROM tasks t INNER JOIN task_developers td ON t.id = td.task_id " +
                    "WHERE td.user_id = :userId AND t.project_id = :projectId",
            nativeQuery = true)
    Page<Task> findByUserIdAndProjectId(@Param("userId") long userId, @Param("projectId") Long projectId, Pageable pageable);
}
