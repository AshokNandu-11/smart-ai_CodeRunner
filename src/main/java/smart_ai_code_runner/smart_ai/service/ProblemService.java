package smart_ai_code_runner.smart_ai.service;

import smart_ai_code_runner.smart_ai.entity.Problem;

import java.util.List;

public interface ProblemService {
    List<Problem> getAllProblems();
    Problem getProblemById(Long id);
}
