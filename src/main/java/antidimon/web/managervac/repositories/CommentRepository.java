package antidimon.web.managervac.repositories;

import antidimon.web.managervac.models.entities.Comment;
import antidimon.web.managervac.models.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTask(Task task);
}
