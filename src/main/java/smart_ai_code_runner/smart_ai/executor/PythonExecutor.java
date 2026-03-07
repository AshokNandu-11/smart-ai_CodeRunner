package smart_ai_code_runner.smart_ai.executor;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class PythonExecutor {
    public String execute(String code, String input) throws Exception {

        Path tempFile = Files.createTempFile("code", ".py");
        Files.write(tempFile, code.getBytes());

        ProcessBuilder processBuilder = new ProcessBuilder(
                "docker", "run", "--rm","-i",
                "-v", tempFile.getParent().toAbsolutePath() + ":/app",
                "python:3.10",
                "python", "/app/" + tempFile.getFileName().toString()
        );

        Process process = processBuilder.start();

        // Send input to program
        if (input != null && !input.isEmpty()) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    process.getOutputStream()));
            writer.write(input);
            writer.flush();
            writer.close();
        }

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );

        StringBuilder output = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line);
        }

        //Read errors
        BufferedReader errorReader = new BufferedReader(new
                InputStreamReader(process.getErrorStream()));
        StringBuilder errorOutput = new StringBuilder();
        while ((line = errorReader.readLine())!= null){
            errorOutput.append(line);
        }

        process.waitFor();

        //If error exists return it

        if (!errorOutput.isEmpty()){
            return "ERROR: "+errorOutput.toString();
        }

        return output.toString();
    }

}
