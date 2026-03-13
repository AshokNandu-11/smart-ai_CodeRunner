package smart_ai_code_runner.smart_ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smart_ai_code_runner.smart_ai.entity.Problem;

public interface ProblemRepository extends JpaRepository<Problem,Long> {
}
