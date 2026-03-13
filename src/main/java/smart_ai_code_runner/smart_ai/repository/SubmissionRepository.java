package smart_ai_code_runner.smart_ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smart_ai_code_runner.smart_ai.entity.Submission;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission,Long> {
    List<Submission> findByProblemIdOrderBySubmittedAtDesc(Long problemId);
}
