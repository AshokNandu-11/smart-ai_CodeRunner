package smart_ai_code_runner.smart_ai.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smart_ai_code_runner.smart_ai.dto.CodeExecutionRequest;
import smart_ai_code_runner.smart_ai.dto.CodeExecutionResponse;
import smart_ai_code_runner.smart_ai.service.CodeRunnerService;


@RestController
@RequestMapping("/run")
@RequiredArgsConstructor
public class CodeExecutionController {

    private final CodeRunnerService codeRunnerService;

    @PostMapping
    public CodeExecutionResponse runCode(@RequestBody CodeExecutionRequest request){
        return codeRunnerService.runCode(request);
    }
}
