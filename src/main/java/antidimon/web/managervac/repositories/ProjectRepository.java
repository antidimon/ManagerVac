package antidimon.web.managervac.repositories;

import antidimon.web.managervac.models.entities.Project;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByName(String projectName);

    @Query(value = "SELECT p.project_name FROM projects AS p LEFT JOIN project_members AS pm ON p.id = pm.project_id " +
            "WHERE pm.user_id = :userId AND pm.role = 'ADMIN'", nativeQuery = true)
    List<String> findNamesByOwner(@Param("userId") long ownerId);


    @Query(value = "SELECT p.* FROM projects AS p LEFT JOIN project_members AS pm ON p.id = pm.project_id " +
            "WHERE pm.user_id = :userId", nativeQuery = true)
    List<Project> findAllUserProjects(long userId);
}
