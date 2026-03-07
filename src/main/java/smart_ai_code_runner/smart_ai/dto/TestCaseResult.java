package smart_ai_code_runner.smart_ai.dto;

import lombok.Data;

@Data

public class TestCaseResult {
    private String input;

    private String expected;

    private String actual;

    private boolean passed;
}
