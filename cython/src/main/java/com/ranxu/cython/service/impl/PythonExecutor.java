package com.ranxu.cython.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author ranxu
 * @Description 在java环境中执行 Ω
 * @Date 2024/2/27 16:05
 */
public class PythonExecutor {
    public static void main(String[] args) {
        String pythonCode = "import numpy as np\n" +
                "\n" +
                "# 创建一个numpy数组\n" +
                "arr = np.array([1, 2, 3, 4, 5])\n" +
                "\n" +
                "# 打印原始数组\n" +
                "print(\"Original array:\", arr)\n" +
                "\n" +
                "# 对数组中的每个元素加10\n" +
                "arr_plus_ten = arr + 10\n" +
                "print(\"Array after adding 10 to each element:\", arr_plus_ten)\n" +
                "\n" +
                "# 计算数组的平均值\n" +
                "mean_value = np.mean(arr)\n" +
                "print(\"Mean of the array:\", mean_value)\n" +
                "\n" +
                "# 计算数组的标准差\n" +
                "std_deviation = np.std(arr)\n" +
                "print(\"Standard deviation of the array:\", std_deviation)\n" +
                "\n" +
                "# 创建一个二维数组\n" +
                "matrix = np.array([[1, 2, 3], [4, 5, 6], [7, 8, 9]])\n" +
                "\n" +
                "# 打印二维数组\n" +
                "print(\"2D Array (Matrix):\")\n" +
                "print(matrix)\n" +
                "\n" +
                "# 计算二维数组每列的和\n" +
                "column_sum = np.sum(matrix, axis=0)\n" +
                "print(\"Sum of each column in the matrix:\", column_sum)\n" +
                "\n" +
                "# 计算二维数组每行的和\n" +
                "row_sum = np.sum(matrix, axis=1)\n" +
                "print(\"Sum of each row in the matrix:\", row_sum)";
        String[] command;

        // 根据操作系统构建命令
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            // Windows系统
            command = new String[]{"cmd", "/c", "echo", pythonCode, "|", "python3"};
        } else {
            // Unix/Linux/Mac系统
            command = new String[]{"bash", "-c", "echo '" + pythonCode + "' | python3"};
        }

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            Process process = pb.start();

            // 读取并打印Python脚本的输出
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

            // 等待进程结束并获取退出值
            int exitCode = process.waitFor();
            System.out.println("Process exited with code " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
