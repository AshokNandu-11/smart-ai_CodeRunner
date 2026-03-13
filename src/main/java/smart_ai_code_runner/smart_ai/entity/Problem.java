package smart_ai_code_runner.smart_ai.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.context.metrics.buffering.StartupTimeline;

@Entity
@Data
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String difficulty;

    @Column(columnDefinition = "TEXT")
    private String inputFormat;

    @Column(columnDefinition = "TEXT")
    private String outputFormat;

    @Column(columnDefinition = "TEXT")
    private String constraints;

    @Column(columnDefinition = "TEXT")
    private String exampleInput;

    @Column(columnDefinition = "TEXT")
    private String exampleOutput;
}
