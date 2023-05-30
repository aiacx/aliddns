package cc.lqt.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecUtil {
    public static void main(String[] args){

    }

    /**
     *   执行命令并获取输出结果
     * */
    public static void runf(String command){
        try {
            Process process = Runtime.getRuntime().exec(command); // 执行命令
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())); // 获取命令输出流
            String line;
            while ((line = reader.readLine()) != null) { // 逐行读取输出结果
                System.out.println(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *   执行命令并等待命令执行完成
     * */
    public static int run(String command){
        try {
            Process process = Runtime.getRuntime().exec(command); // 执行命令
            int exitCode = process.waitFor(); // 等待命令执行完成并获取退出码
            return exitCode;
            //System.out.println("Command exited with code " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return -999;
    }

    public static void runa(String command){
        try {
            Process process = Runtime.getRuntime().exec("nircmd.exe elevate " + command); // 执行命令
            int exitCode = process.waitFor(); // 等待命令执行完成并获取退出码
            //System.out.println("Command exited with code " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
