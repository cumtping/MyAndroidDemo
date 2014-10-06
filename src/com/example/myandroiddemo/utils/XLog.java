package com.example.myandroiddemo.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.Log;

public class XLog {
    private static final String TAG = "XLog";

    /**
     * 打印log级别，5verbose; 4debug; 3info; 2warning; 1error; 0not print.
     */
    public static final int gLogLevel = 5;

    /**
     * verbose对应级别
     */
    public static final int LEVEL_VERBOSE = 5;

    /**
     * dubug对应级别
     */
    public static final int LEVEL_DEBUG = 4;

    /**
     * info对应级别
     */
    public static final int LEVEL_INFO = 3;

    /**
     * warning对应级别
     */
    public static final int LEVEL_WARNING = 2;

    /**
     * error对应级别
     */
    public static final int LEVEL_ERROR = 1;

    /**
     * 不打印
     */
    public static final int LEVEL_NOT_PRINT = 0;

    /**
     * 功能描述: 打印verbose级别Log<br>
     * 〈功能详细描述〉
     * 
     * @param logContent
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static void printLogV(String logContent) {
        if (gLogLevel >= LEVEL_VERBOSE) {
            String log = getCallerInfo() + logContent;
            Log.v(TAG, log);
        }
    }

    /**
     * 功能描述: 打印debug级别Log<br>
     * 〈功能详细描述〉
     * 
     * @param logContent
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static void printLogD(String logContent) {
        if (gLogLevel >= LEVEL_DEBUG) {
            String log = getCallerInfo() + logContent;
            Log.d(TAG, log);
        }
    }

    /**
     * 功能描述: 打印info级别Log<br>
     * 〈功能详细描述〉
     * 
     * @param logContent
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static void printLogI(String logContent) {
        if (gLogLevel >= LEVEL_INFO) {
            String log = getCallerInfo() + logContent;
            Log.i(TAG, log);
        }
    }

    /**
     * 功能描述: 打印warning级别Log<br>
     * 〈功能详细描述〉
     * 
     * @param logContent
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static void printLogW(String logContent) {
        if (gLogLevel >= LEVEL_WARNING) {
            String log = getCallerInfo() + logContent;
            Log.w(TAG, log);
        }
    }

    /**
     * 
     * 功能描述: 打印Exception异常信息<br>
     * 〈功能详细描述〉
     * 
     * @param logContent
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static void printLogE(String logContent) {
        if (gLogLevel >= LEVEL_ERROR) {
            String log = getCallerInfo() + logContent;
            Log.e(TAG, log);
        }
    }

    /**
     * 
     * 功能描述: 打印Throwable(Exception、Error)信息<br>
     * 〈功能详细描述〉
     * 
     * @param logContent
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static void printLogThrowable(Throwable e) {
        if (gLogLevel >= LEVEL_ERROR) {
            String log = getCallerInfo() + e;
            Log.e(TAG, log);
            if (e != null) {
                Log.e(TAG, getInfoFromThrowable(e));
            }
        }
    }

    public static String getInfoFromThrowable(Throwable e) {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return "\r\n" + sw.toString();

    }

    /**
     * 
     * 功能描述: 获取调用方法信息<br>
     * 〈功能详细描述〉用于log输出，可以获取打印log的方法名、文件名及行号等。
     * 
     * @return 调用方法信息，格式为“文件名.方法名（Line行号）:”
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String getCallerInfo() {
        StackTraceElement stack[] = Thread.currentThread().getStackTrace();
        if (stack != null && stack.length > 4) {
            StackTraceElement ste = stack[4];
            String name = ste.getFileName();
            if (name != null) {
                int suffixIndex = name.indexOf('.');
                if (suffixIndex > 0) {
                    name = name.substring(0, suffixIndex);
                }
            }
            String callerInfo = name + "." + ste.getMethodName() + "(Line" + ste.getLineNumber() + "): ";
            return callerInfo;
        }
        return "";
    }
}
