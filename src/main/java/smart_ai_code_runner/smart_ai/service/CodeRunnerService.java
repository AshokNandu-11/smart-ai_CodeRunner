package smart_ai_code_runner.smart_ai.service;

import smart_ai_code_runner.smart_ai.dto.CodeExecutionRequest;
import smart_ai_code_runner.smart_ai.dto.CodeExecutionResponse;

public interface CodeRunnerService {

    CodeExecutionResponse runCode(CodeExecutionRequest request);
}
