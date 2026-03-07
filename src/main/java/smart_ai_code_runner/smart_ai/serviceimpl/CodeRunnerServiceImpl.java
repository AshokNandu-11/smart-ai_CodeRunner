package smart_ai_code_runner.smart_ai.serviceimpl;


import org.springframework.stereotype.Service;
import smart_ai_code_runner.smart_ai.dto.CodeExecutionRequest;
import smart_ai_code_runner.smart_ai.dto.CodeExecutionResponse;
import smart_ai_code_runner.smart_ai.dto.TestCaseResult;
import smart_ai_code_runner.smart_ai.entity.TestCase;
import smart_ai_code_runner.smart_ai.executor.PythonExecutor;
import smart_ai_code_runner.smart_ai.repository.TestCasesRepository;
import smart_ai_code_runner.smart_ai.service.CodeRunnerService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CodeRunnerServiceImpl implements CodeRunnerService {

    private final PythonExecutor pythonExecutor;
    private final TestCasesRepository testCasesRepository;

    public CodeRunnerServiceImpl(PythonExecutor pythonExecutor, TestCasesRepository testCasesRepository) {
        this.pythonExecutor = pythonExecutor;
        this.testCasesRepository = testCasesRepository;
    }



    @Override
    public CodeExecutionResponse runCode(CodeExecutionRequest request){

        CodeExecutionResponse response = new CodeExecutionResponse();

        List<TestCaseResult> results = new ArrayList<>();

        try {

            // Fetch all testcases for this question
            List<TestCase> testCases = testCasesRepository.findByQuestionId(request.getQuestionId());

            int passedCount = 0;

            for(TestCase testCase : testCases){

                String input = testCase.getInput();
                String expectedOutput = testCase.getExpectedOutput();

                String actualOutput = "";

                if("python".equalsIgnoreCase(request.getLanguage())){

                    actualOutput = pythonExecutor.execute(request.getSourceCode(), input);

                }else{

                    response.setSuccess(false);
                    response.setError("Language not supported yet");
                    return response;
                }

                boolean passed = actualOutput.trim().equals(expectedOutput.trim());

                if(passed){
                    passedCount++;
                }

                TestCaseResult result = new TestCaseResult();
                result.setInput(input);
                result.setExpected(expectedOutput);
                result.setActual(actualOutput);
                result.setPassed(passed);

                results.add(result);
            }

            response.setSuccess(true);
            response.setResults(results);
            response.setTotalTestCases(testCases.size());
            response.setPassedTestCases(passedCount);

        }catch(Exception e){

            response.setSuccess(false);
            response.setError(e.getMessage());

        }

        return response;
    }

}
