package smart_ai_code_runner.smart_ai.dto;

import lombok.Data;

@Data
public class CodeExecutionRequest {

    private String language;
    private String sourceCode;
    private String input;
    private String expectedOutput;
    private Long questionId;

}
