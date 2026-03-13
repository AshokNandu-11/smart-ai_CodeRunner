package smart_ai_code_runner.smart_ai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smart_ai_code_runner.smart_ai.entity.Problem;
import smart_ai_code_runner.smart_ai.service.ProblemService;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping
    public List<Problem> getAllProblems(){
        return problemService.getAllProblems();
    }
    @GetMapping("/{id}")
    public Problem getProblem(@PathVariable Long id){
        return problemService.getProblemById(id);
    }
}
