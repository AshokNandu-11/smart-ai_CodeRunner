package smart_ai_code_runner.smart_ai.serviceimpl;



import org.springframework.stereotype.Service;
import smart_ai_code_runner.smart_ai.dto.CodeExecutionRequest;
import smart_ai_code_runner.smart_ai.dto.CodeExecutionResponse;
import smart_ai_code_runner.smart_ai.dto.TestCaseResult;
import smart_ai_code_runner.smart_ai.entity.Submission;
import smart_ai_code_runner.smart_ai.entity.TestCase;
import smart_ai_code_runner.smart_ai.executor.JavaExecutor;
import smart_ai_code_runner.smart_ai.executor.PythonExecutor;
import smart_ai_code_runner.smart_ai.repository.SubmissionRepository;
import smart_ai_code_runner.smart_ai.repository.TestCaseRepository;
import smart_ai_code_runner.smart_ai.service.CodeRunnerService;

import java.beans.DesignMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class CodeRunnerServiceImpl implements CodeRunnerService {

    private final PythonExecutor pythonExecutor;
    private final JavaExecutor javaExecutor;
    private final TestCaseRepository testCaseRepository;
    private final SubmissionRepository submissionRepository;

    public CodeRunnerServiceImpl(PythonExecutor pythonExecutor, TestCaseRepository testCaseRepository, SubmissionRepository submissionRepository, JavaExecutor javaExecutor) {
        this.pythonExecutor = pythonExecutor;
        this.testCaseRepository = testCaseRepository;
        this.submissionRepository = submissionRepository;
        this.javaExecutor = javaExecutor;
    }



    @Override
    public CodeExecutionResponse runCode(CodeExecutionRequest request){

        long startTime = System.currentTimeMillis();
        CodeExecutionResponse response = new CodeExecutionResponse();

        List<TestCaseResult> results = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        try {

            List<TestCase> testCases = testCaseRepository.findByQuestionId(request.getQuestionId());
            System.out.println("Total Testcases Found: "+ testCases.size());

            int passedCount = 0;

            List<Future<TestCaseResult>> futures = new ArrayList<>();

            for(TestCase testCase : testCases){

                Future<TestCaseResult> future = executorService.submit(() -> {

                    String input = testCase.getInput();
                    String expectedOutput = testCase.getExpectedOutput();

                    String actualOutput = "";

                    if("python".equalsIgnoreCase(request.getLanguage())){
                        actualOutput = pythonExecutor.execute(request.getSourceCode(), input);
                    } else if ("java".equalsIgnoreCase(request.getLanguage())) {
                        actualOutput = javaExecutor.execute(request.getSourceCode(),input);

                    } else{
                        throw new RuntimeException("Language not supported yet");
                    }

                    boolean passed = actualOutput.trim().equals(expectedOutput.trim());

                    TestCaseResult result = new TestCaseResult();
                    result.setInput(input);
                    result.setExpected(expectedOutput);
                    result.setActual(actualOutput);
                    result.setPassed(passed);

                    return result;
                });

                futures.add(future);
            }

            int index = 0;

            for(Future<TestCaseResult> future : futures){

                TestCaseResult result = future.get();
                TestCase testCase = testCases.get(index++);

                if(result.isPassed()){
                    passedCount++;
                }

                if(testCase.isVisible()){
                    results.add(result);
                }
            }
            Submission submission = new Submission();
            submission.setQuestionId(request.getQuestionId());
            submission.setSourceCode(request.getSourceCode());
            submission.setLanguage(request.getLanguage());
            submission.setPassed(response.isPassed());
            submission.setTotalTestCases(response.getTotalTestCases());
            submission.setPassedTestCases(response.getPassedTestCases());
            submission.setExecutionTime(response.getExecutionTime());
            submission.setSubmittedAt(LocalDateTime.now());

            submissionRepository.save(submission);

            response.setSuccess(true);
            response.setResults(results);

            int totalTestCases = testCases.size();

            response.setTotalTestCases(totalTestCases);
            response.setPassedTestCases(passedCount);
            response.setFailedTestCases(totalTestCases - passedCount);
            response.setPassed(passedCount == totalTestCases);

            long endTime = System.currentTimeMillis();
            response.setExecutionTime(endTime-startTime);

            executorService.shutdown();

            if (passedCount == testCases.size()){
                response.setStatus("ACCEPTED");
                response.setMessage("All test cases passed");

            } else if (passedCount>0) {
                response.setStatus("PARTIAL_SUCCESS");
                response.setMessage("Some test cases failed");
            }else {
                response.setStatus("WRONG_ANSWER");
                response.setMessage("All test cases failed");
            }

        }catch(Exception e){

            response.setSuccess(false);
            response.setError(e.getMessage());

        }

        return response;
    }


}
