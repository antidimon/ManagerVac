package antidimon.web.managervac.controllers;


import antidimon.web.managervac.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {

    private CommentService commentService;

}
