package smart_ai_code_runner.smart_ai.serviceimpl;

import org.springframework.stereotype.Service;
import smart_ai_code_runner.smart_ai.entity.Problem;
import smart_ai_code_runner.smart_ai.repository.ProblemRepository;
import smart_ai_code_runner.smart_ai.service.ProblemService;

import java.util.List;

@Service
public class ProblemServiceImpl implements ProblemService {
    private final ProblemRepository problemRepository;

    public ProblemServiceImpl(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    @Override
    public List<Problem> getAllProblems(){
        return problemRepository.findAll();
    }
    @Override
    public Problem getProblemById(Long id){
        return problemRepository.findById(id).orElse(null);
    }
}
