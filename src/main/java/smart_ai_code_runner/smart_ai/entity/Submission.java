package smart_ai_code_runner.smart_ai.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.bytecode.enhance.spi.EnhancementInfo;

import java.time.LocalDateTime;

@Entity
@Data
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long questionId;

    @Column(columnDefinition = "TEXT")
    private String sourceCode;

    private String language;

    private boolean passed;
    private int totalTestCases;
    private int passedTestCases;
    private long executionTime;
    private LocalDateTime submittedAt;
}

