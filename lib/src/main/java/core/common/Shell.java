package core.common;

import android.util.Log;

import core.common.tuple.Tuple;
import core.common.tuple.Tuple3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Shell {

    public static Integer runCommand(String command) {
        return runCommand(command, false, false, "su").v1;
    }

    public static Tuple3<Integer, String, String> runCommandAndReturn(String command) {
        return runCommand(command, false, true, "su");
    }

    public static Integer runRootCommand(String command) {
        return runCommand(command, true, false, "su").v1;
    }

    public static Tuple3<Integer, String, String> runRootCommandAndReturn(String command) {
        return runCommand(command, true, true, "su");
    }

    public static Tuple3<Integer, String, String> runCommand(String command, boolean isRoot, boolean isNeedResult, String suPath) {
        Log.i("runCommand", "command=" + command + "|isRoot=" + isRoot + "|isNeedResult=" + isNeedResult + "|suPath=" + suPath);
        Process process = null;
        DataOutputStream os = null;
        DataInputStream stdOut = null;
        DataInputStream stdErr = null;
        Tuple3<Integer, String, String> t;
        try {
            StringBuffer output = new StringBuffer();
            StringBuffer error = new StringBuffer();
            if (isRoot) {
                process = Runtime.getRuntime().exec("su");
                os = new DataOutputStream(process.getOutputStream());
                os.writeBytes(command + "\n");
                os.writeBytes("exit\n");
                os.flush();
            } else {
                String[] commands = new String[]{"sh", "-c", command};
                process = Runtime.getRuntime().exec(commands);
            }
            if (isNeedResult) {
                stdOut = new DataInputStream(process.getInputStream());
                String line;
                while ((line = stdOut.readLine()) != null) {
                    output.append(line).append('\n');
                }
                stdErr = new DataInputStream(process.getErrorStream());
                while ((line = stdErr.readLine()) != null) {
                    error.append(line).append('\n');
                }
            }
            process.waitFor();
            t = Tuple.tuple(process.exitValue(), output.toString().trim(), error.toString().trim());
        } catch (IOException | InterruptedException e) {
            t = Tuple.tuple(-1, "", e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (stdOut != null) {
                    stdOut.close();
                }
                if (stdErr != null) {
                    stdErr.close();
                }
                process.destroy();
            } catch (IOException e) {
                t = Tuple.tuple(-1, "", e.getMessage());
            }
        }
        return t;
    }
}
