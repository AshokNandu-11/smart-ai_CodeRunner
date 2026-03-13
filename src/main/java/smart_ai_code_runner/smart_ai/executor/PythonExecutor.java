package smart_ai_code_runner.smart_ai.executor;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class PythonExecutor {

    public String execute(String sourceCode, String input) throws Exception {

        // create temporary folder
        String folder = System.getProperty("java.io.tmpdir") + "/judge_" + UUID.randomUUID();
        File dir = new File(folder);
        dir.mkdirs();

        // create python file
        Path codeFile = Path.of(folder, "solution.py");
        Files.writeString(codeFile, sourceCode);

        // build docker command
        ProcessBuilder processBuilder = new ProcessBuilder(
                "docker",
                "run",
                "--rm",
                "-i",
                "-v",
                folder + ":/app",
                "python:3.10",
                "python",
                "/app/solution.py"
        );

        Process process = processBuilder.start();

        // send input
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(process.getOutputStream())
        );

        writer.write(input);
        writer.newLine();
        writer.flush();
        writer.close();

        // read output
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );

        // read error stream
        BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(process.getErrorStream())
        );

        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line);
        }

        while ((line = errorReader.readLine()) != null) {
            error.append(line);
        }

        // timeout protection (3 seconds)
        boolean finished = process.waitFor(3, TimeUnit.SECONDS);

        if (!finished) {
            process.destroyForcibly();
            return "TIME_LIMIT_EXCEEDED";
        }
        int exitCode = process.exitValue();

        // if python error occurs
        if (!error.isEmpty()) {
            return "RUNTIME_ERROR: " + error.toString();
        }


        return output.toString().trim();
    }
}
