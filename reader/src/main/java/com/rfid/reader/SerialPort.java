package com.rfid.reader;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//



import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {
    private static final String TAG = "SerialPort";
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    public SerialPort(File device, int baudrate) throws SecurityException, IOException {
        if (!device.canRead() || !device.canWrite()) {
            try {
                Process su = Runtime.getRuntime().exec("/system/xbin/su");
                String cmd = "chmod 666 " + device.getAbsolutePath() + "\nexit\n";
                su.getOutputStream().write(cmd.getBytes());
                su.getOutputStream().flush();
                if (su.waitFor() != 0 || !device.canRead() || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception var5) {
                var5.printStackTrace();
                throw new SecurityException();
            }
        }

        this.mFd = this.open(device.getAbsolutePath(), baudrate);
        if (this.mFd == null) {
            throw new IOException();
        } else {
            this.mFileInputStream = new FileInputStream(this.mFd);
            this.mFileOutputStream = new FileOutputStream(this.mFd);
        }
    }

    public InputStream getInputStream() {
        return this.mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return this.mFileOutputStream;
    }

    private native FileDescriptor open(String var1, int var2);

    public native void close();

    static {
        System.loadLibrary("serial_port");
    }
}
