package antidimon.web.managervac.services;

import antidimon.web.managervac.repositories.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {

    private CommentRepository commentRepository;

}
