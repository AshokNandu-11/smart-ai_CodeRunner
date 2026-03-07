package smart_ai_code_runner.smart_ai.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import smart_ai_code_runner.smart_ai.entity.TestCase;

import java.util.List;

public interface TestCasesRepository extends JpaRepository<TestCase,Long> {
    List<TestCase> findByQuestionId(Long questionId);

}
