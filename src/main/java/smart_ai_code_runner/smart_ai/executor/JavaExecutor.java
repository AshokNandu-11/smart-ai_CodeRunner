package smart_ai_code_runner.smart_ai.executor;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JavaExecutor {

    public String execute(String sourceCode, String input) throws Exception{

        //create temp folder
        String folder = System.getProperty("java.io.tmpdir") + "/judge_" + UUID.randomUUID();
        File dir = new File(folder);
        dir.mkdirs();

        //write JAVA file

        Path codeFile = Path.of(folder, "Main.java");
        Files.writeString(codeFile,sourceCode);

        ProcessBuilder processBuilder = new ProcessBuilder(
                "docker",
                "run",
                "--rm",
                "-i",
                "--memory=128m",
                "--cpus=0.5",
                "--network=none",
                "-v",
                folder + ":/app",
                "eclipse-temurin:17",
                "bash",
                "-c",
                "javac /app/Main.java && java -cp /app Main"
        );

        Process process = processBuilder.start();

        //send input
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(process.getOutputStream())
        );
        writer.write(input);
        writer.newLine();
        writer.flush();
        writer.close();

        //read Output

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );
        BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(process.getErrorStream())
        );

        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null){
            output.append(line);

        }
        while ((line = errorReader.readLine())!= null){
            error.append(line);
        }
        boolean finished = process.waitFor(5, TimeUnit.SECONDS);

        if(!finished){
            process.destroyForcibly();
            return "TIME_LIMIT_EXCEEDED";
        }
        if (!error.isEmpty()){
            return "ERROR: "+error.toString();
        }
        return output.toString().trim();
    }
}
