package antidimon.web.managervac.services;


import antidimon.web.managervac.repositories.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectService {

    private ProjectRepository projectRepository;

}
