package fr.nocloud.maestro.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ProcessUtils {


    public static void exec(String command, List<String> params) {
        exec(command, params.toArray(new String[0]));
    }

    public static void exec(String command, String... params) {
        int exitValue = command(command, params);

        if(exitValue != 0) {
            throw new RuntimeException("Error executing command [" + command + " " + String.join(" ", params) + "]");
        }
    }

    private static int command(String command, String... params) {

        String cmd = command + " " + String.join(" ", params);

        try {
            System.out.println("Command Execution : [" + cmd + "]");

            Process process = Runtime.getRuntime().exec(cmd);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String s;
            while ((s = reader.readLine()) != null) {
                System.out.println(s);
            }

            process.waitFor();

            return process.exitValue();

        } catch (IOException e) {
            throw new RuntimeException("Unable to perform on process output stream.", e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Unable to perform process execution.", e);
        }
    }
}
