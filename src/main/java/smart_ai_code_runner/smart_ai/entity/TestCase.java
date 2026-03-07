package smart_ai_code_runner.smart_ai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long questionId;

    @Column(columnDefinition = "TEXT")
    private String input;

    @Column(columnDefinition = "TEXT")
    private String expectedOutput;

    private boolean sample;


}
