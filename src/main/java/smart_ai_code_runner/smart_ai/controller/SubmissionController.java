package smart_ai_code_runner.smart_ai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smart_ai_code_runner.smart_ai.entity.Submission;
import smart_ai_code_runner.smart_ai.repository.SubmissionRepository;

import java.util.List;


@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {
    private final SubmissionRepository submissionRepository;

    public SubmissionController(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    @GetMapping
    public List<Submission> getAllSubmissions(){
        return submissionRepository.findAll();
    }
    @GetMapping("/problem/{problemId}")
    public List<Submission> getSubmissionsByProblem(@PathVariable Long problemId){
        return submissionRepository.findByProblemIdOrderBySubmittedAtDesc(problemId);
    }
}
