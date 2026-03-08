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

        // Create temp directory
        String folder = System.getProperty("java.io.tmpdir") + "/judge_" + UUID.randomUUID();
        File dir = new File(folder);
        dir.mkdirs();

        // Create python file
        Path codeFile = Path.of(folder, "solution.py");
        Files.writeString(codeFile, sourceCode);

        // Docker command
        ProcessBuilder pb = new ProcessBuilder(
                "docker",
                "run",
                "--rm",
                "-i",
                "-v", folder + ":/app",
                "python:3.10",
                "python",
                "/app/solution.py"
        );

        Process process = pb.start();

        // Send input
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(process.getOutputStream())
        );

        writer.write(input);
        writer.newLine();
        writer.flush();
        writer.close();

        // Capture output
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );

        StringBuilder output = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line);
        }

        process.waitFor(2, TimeUnit.SECONDS);

        return output.toString();
    }
}
