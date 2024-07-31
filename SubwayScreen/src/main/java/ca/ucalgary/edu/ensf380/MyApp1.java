package ca.ucalgary.edu.ensf380;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Author: Mahdi Ansari
 */
public class MyApp1 {
    public static void main(String[] args) {
        System.out.println("Starting MyApp1...");

        // Define the command to run the simulator
        String[] command = {
            "java", "-jar", "../exe/SubwaySimulator.jar", 
            "--in", "../data/subway.csv", 
            "--out", "../out"
        };

        // Log the command being executed
        System.out.println("Command to execute: " + String.join(" ", command));
        
        // Verify file paths before starting the process
        verifyFilePaths();

        // Clear the output directory
        clearOutputDirectory("../out");

        // Runs the simulator
        Process process = null;
        try {
            process = new ProcessBuilder(command).start();
            System.out.println("Simulator process started.");
        } catch (IOException e) {
            System.err.println("Failed to start the simulator process.");
            e.printStackTrace();
            return;
        }

        final Process finalProcess = process;

        // It will destroy the simulator process at the end
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (finalProcess != null) {
                finalProcess.destroy();
                System.out.println("Simulator process destroyed.");
            }
        }));

        // Keep the application alive for 5 minutes
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (finalProcess != null) {
                    finalProcess.destroy();
                    System.out.println("Timer ended: Simulator process destroyed.");
                }
                timer.cancel();
            }
        }, 5 * 60 * 1000); // 5 minutes in milliseconds

        // Prints simulator output to the console
        readProcessOutput(process);
        readProcessError(process);
    }

    private static void verifyFilePaths() {
        String[] paths = {
            "../exe/SubwaySimulator.jar",
            "../data/subway.csv",
            "../out"
        };

        for (String path : paths) {
            File file = new File(path);
            if (file.exists()) {
                System.out.println("Path exists: " + path);
                if (file.isDirectory()) {
                    System.out.println("It's a directory.");
                } else if (file.isFile()) {
                    System.out.println("It's a file.");
                }
            } else {
                System.err.println("Path does not exist: " + path);
            }
        }
    }

    private static void clearOutputDirectory(String directoryPath) {
        try {
            Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .forEach(File::delete);
            System.out.println("Output directory cleared: " + directoryPath);
        } catch (IOException e) {
            System.err.println("Failed to clear output directory: " + directoryPath);
            e.printStackTrace();
        }
    }

    private static void readProcessOutput(Process process) {
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            System.out.println("Reading simulator output...");
            while ((line = reader.readLine()) != null) {
                System.out.println("Simulator Output: " + line);
            }
            System.out.println("Finished reading simulator output.");
        } catch (IOException e) {
            System.err.println("Error reading simulator output.");
            e.printStackTrace();
        }
    }

    private static void readProcessError(Process process) {
        InputStream errorStream = process.getErrorStream();
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
        String line;
        try {
            System.out.println("Reading simulator error output...");
            while ((line = errorReader.readLine()) != null) {
                System.err.println("Simulator Error: " + line);
            }
        } catch (IOException e) {
            System.err.println("Error reading simulator error stream.");
            e.printStackTrace();
        }
    }
}
