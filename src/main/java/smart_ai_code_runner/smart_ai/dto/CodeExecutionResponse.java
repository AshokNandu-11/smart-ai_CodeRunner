package smart_ai_code_runner.smart_ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class CodeExecutionResponse {

    private String output;
    private boolean success;
    private String error;
    private boolean passed;
    private String actualOutput;
    private int totalTestCases;
    private int passedTestCases;
    private List<TestCaseResult> results;
    private int failedTestCases;
    private long executionTime;
    private String status;
    private String message;
}
