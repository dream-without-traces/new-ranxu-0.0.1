package com.ranxu.cython.service.impl;

import org.bytedeco.cpython.PyObject;
import org.bytedeco.cpython.global.python;

/**
 * @Author ranxu
 * @Description
 * @Date 2024/2/27 16:05
 */
public class PythonExecutor {
    public static void main(String[] args) {
        // 初始化 Python 解释器
        python.Py_Initialize();
        try {
            // 执行多行 Python 代码
            PyObject pyMain = python.PyImport_AddModule("__main__");
            PyObject pyDict = python.PyModule_GetDict(pyMain);

            python.PyRun_StringFlags(
                    "a = 5\n" +
                            "b = 7\n" +
                            "c = a + b\n",
                    python.Py_file_input, pyDict, pyDict, null
            );

            // 获取特定变量的值
            PyObject pyC = python.PyDict_GetItemString(pyDict, "c");
            if ((pyC != null) && (python.PyNumber_Check(pyC) != 0)) {
                long cValue = python.PyLong_AsLong(pyC);
                System.out.println("Value of c: " + cValue);
            } else {
                System.out.println("Variable 'c' not found or is not a number.");
            }
        } finally {
            // 清理并关闭 Python 解释器
            python.Py_Finalize();
        }
    }
}
