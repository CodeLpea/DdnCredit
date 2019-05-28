package com.example.lp.ddncredit.rfid;


import com.rfid.reader.Reader;

import java.io.IOException;
import java.security.InvalidParameterException;
/**
 * 获取Reader实例的单例
 * lp
 * 2019/05/27
 * */
public class ReaderIml {
    public static Reader reader = null;
    private static ReaderIml instance;
    // 单例模式中获取唯一的ReaderIml实例
    public static ReaderIml getInstance() {
        if (instance == null) {
            instance = new ReaderIml();
        }
        return instance;
    }

    public Reader getReader() throws SecurityException, IOException, InvalidParameterException {
        if (reader == null) {
            reader = Reader.getInstance("/dev/ttyS1" , 9600);
        }
        return reader;
    }

    public void closeReader() {
        if (reader != null) {
            reader.close();
            reader = null;
        }
    }

}
