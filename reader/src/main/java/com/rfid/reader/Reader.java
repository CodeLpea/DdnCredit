package com.rfid.reader;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Reader {
    private final String TAG = Reader.class.getSimpleName();
    private final boolean DEBUG = true;
    private final long TIMEOUT = 1000L;
    private final byte STX = -86;
    private final byte ETX = -69;
    private final byte ADDR = 0;
    private final byte ERR_SUCCESS = 0;
    private final byte ERR_FAILURE = 1;
    private final byte ERR_WRITE = 2;
    private final byte ERR_READ = 3;
    private final byte ERR_TIMEOUT = 4;
    private final byte ERR_FORMAT = 5;
    private final byte CMD_SYSTEM_GETVER = -122;
    private final byte CMD_ISO14443A_REQ = 3;
    private final byte CMD_ISO14443A_READ = 32;
    private final byte CMD_ISO14443A_WRITE = 33;
    private final byte CMD_ISO14443A_GETUID = 37;
    private final byte CMD_ISO14443A_RATS = 39;
    private final byte CMD_ISO14443A_APDU = 40;
    private final byte CMD_ISO14443B_GETUID = 9;
    private final byte CMD_ISO14443B_APDU = 13;
    private final byte CMD_ISO15693_INVENTORY = 16;
    private final byte CMD_ISO15693_READBLK = 17;
    private final byte CMD_ISO15693_WRITEBLK = 18;
    private final byte CMD_ISO15693_LOCKBLK = 19;
    private String devPath;
    private int devBaud;
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private static Reader reader;

    public Reader() {
    }

    public static Reader getInstance(String path, int baudrate) {
        if (null == reader) {
            reader = new Reader();
            reader.devPath = path;
            reader.devBaud = baudrate;
            reader.onCreate();
        } else if (!reader.devPath.equals(path) || reader.devBaud != baudrate) {
            reader.close();
            reader.devPath = path;
            reader.devBaud = baudrate;
            reader.onCreate();
        }

        return reader;
    }

    public InputStream getInputStream() {
        return this.mInputStream;
    }

    public OutputStream getOutputStream() {
        return this.mOutputStream;
    }

    public void onCreate() {
        try {
            this.mSerialPort = new SerialPort(new File(this.devPath), this.devBaud);
            this.mOutputStream = this.mSerialPort.getOutputStream();
            this.mInputStream = this.mSerialPort.getInputStream();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public void close() {
        if (this.mInputStream != null) {
            try {
                this.mInputStream.close();
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        }

        if (this.mOutputStream != null) {
            try {
                this.mOutputStream.close();
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        }

        if (this.mSerialPort != null) {
            this.mSerialPort.close();
            this.mSerialPort = null;
        }

    }

    private byte makeCrc(byte[] data, int startPos, int endPos) {
        byte crc = 0;

        for (int i = startPos; i < endPos; ++i) {
            crc ^= data[i];
        }

        return crc;
    }

    public int optFunction(byte address, byte command, byte[] inData, byte inLen, byte[] outData, byte[] outLen, byte[] errCode) {
        byte[] wBuffer = new byte[inLen + 6];
        byte[] rBuffer = new byte[512];
        byte[] mBuffer = new byte[512];

        int readLen;
        try {
            if (this.mOutputStream == null) {
                return 2;
            }

            wBuffer[0] = -86;
            wBuffer[1] = address;
            wBuffer[2] = (byte) (inLen + 1);
            wBuffer[3] = command;
            if (inData != null && inLen != 0) {
                System.arraycopy(inData, 0, wBuffer, 4, inLen);
            }

            wBuffer[4 + inLen] = this.makeCrc(wBuffer, 1, 4 + inLen);
            wBuffer[5 + inLen] = -69;
            this.mOutputStream.write(wBuffer);
            String str = ">> ";
            readLen = 0;

            while (true) {
                if (readLen >= inLen + 6) {
                    // Log.i(this.TAG, str);
                    break;
                }

                str = str + String.format("%02X ", wBuffer[readLen]);
                ++readLen;

            }
            Log.i(TAG, "optFunction: str" + str);
        } catch (IOException var23) {
            var23.printStackTrace();
            return 2;
        }

        try {
            if (this.mInputStream == null) {
                return 3;
            } else {
                readLen = 0;
                int recvLen = 0;
                long startTime;
                long currTime = startTime = System.currentTimeMillis();

                long endTime;
                for (endTime = startTime + 1000L; currTime < endTime; currTime = System.currentTimeMillis()) {
                    if (this.mInputStream.available() > 0) {
                        int size = this.mInputStream.read(mBuffer);
                        String str = "<< ";

                        for (int i = 0; i < size; ++i) {
                            str = str + String.format("%02X ", mBuffer[i]);
                        }

                        Log.i(this.TAG, str);
                        if (recvLen > 0) {
                            System.arraycopy(mBuffer, 0, rBuffer, readLen, size);
                            readLen += size;
                            if (readLen >= recvLen) {
                                break;
                            }
                        } else {
                            if (readLen == 0) {
                                for (int i = 0; i < size; ++i) {
                                    if (mBuffer[i] == -86) {
                                        System.arraycopy(mBuffer, i, rBuffer, 0, size - i);
                                        readLen += size - i;
                                        break;
                                    }
                                }
                            } else {
                                System.arraycopy(mBuffer, 0, rBuffer, readLen, size);
                                readLen += size;
                            }

                            if (readLen >= 3) {
                                recvLen = rBuffer[2] + 5;
                                if (readLen >= recvLen) {
                                    break;
                                }
                            }
                        }
                    }

                    Thread.sleep(10L);
                }

                if (currTime >= endTime) {
                    Log.e(this.TAG, "Receive data timeout error");
                    return 4;
                } else if (rBuffer[0] == -86 && rBuffer[rBuffer[2] + 4] == -69 && this.makeCrc(rBuffer, 1, rBuffer[2] + 4) == 0) {
                    if (rBuffer[3] == 0) {
                        if (outLen != null && outData != null) {
                            outLen[0] = (byte) (rBuffer[2] - 1);
                            System.arraycopy(rBuffer, 4, outData, 0, outLen[0]);
                        }

                        return 0;
                    } else {
                        errCode[0] = rBuffer[4];
                        return 1;
                    }
                } else {
                    Log.e(this.TAG, "Receive data crc error");
                    return 5;
                }
            }
        } catch (Exception var22) {
            var22.printStackTrace();
            return 3;
        }
    }

    public int optFunction_M2(byte[] outData) {
        byte[] mBuffer = new byte[512];
        try {
            if (this.mInputStream == null) {
                return 3;
            } else {
                int readIdex=50;
                for (int i = 0; i < readIdex; i++) {
                    if(this.mInputStream.available()>0){
                        int size = this.mInputStream.read(mBuffer);
                        Log.i(TAG, "optFunction_M2: size"+size);
                        if (size == 4) {
                            System.arraycopy(mBuffer, 0, outData, 0,size);
                            StringBuilder stringBuffer = new StringBuilder();
                            for (int aR_data : outData) {
                                stringBuffer.append(String.format("%02x", aR_data & 0x00ff));
                            }
                            Log.i(this.TAG, stringBuffer.toString());
                            return 0;
                        }
                    }
                    Thread.sleep(10);
                }
                return 1;
            }
        } catch (Exception var22) {
            var22.printStackTrace();
            return 3;
        }
    }

    public int GetVersion(byte[] version, byte[] verLen, byte[] errCode) {
        return this.optFunction((byte) 0, (byte) -122, (byte[]) null, (byte) 0, version, verLen, errCode);
    }

    public int Iso14443a_Request(byte mode, byte[] atq, byte[] errCode) {
        byte[] inData = new byte[]{mode};
        byte[] outData = new byte[256];
        byte[] outLen = new byte[1];
        int result = this.optFunction((byte) 0, (byte) 3, inData, (byte) inData.length, outData, outLen, errCode);
        if (result == 0) {
            System.arraycopy(outData, 0, atq, 0, outLen[0]);
        }

        return result;
    }

    public int Iso14443a_Read(byte blockAddr, byte blockCnt, byte keyType, byte[] key, byte[] data, byte[] errCode) {
        byte[] inData = new byte[9];
        byte[] outData = new byte[256];
        byte[] outLen = new byte[1];
        inData[0] = keyType;
        inData[1] = blockCnt;
        inData[2] = blockAddr;
        System.arraycopy(key, 0, inData, 3, 6);
        int result = this.optFunction((byte) 0, (byte) 32, inData, (byte) inData.length, outData, outLen, errCode);
        if (result == 0) {
            System.arraycopy(outData, outLen[0] - 16 * blockCnt, data, 0, 16 * blockCnt);
        }

        return result;
    }

    public int Iso14443a_Write(byte blockAddr, byte blockCnt, byte keyType, byte[] key, byte[] data, byte[] errCode) {
        byte[] inData = new byte[9 + blockCnt * 16];
        byte[] outData = new byte[256];
        byte[] outLen = new byte[1];
        inData[0] = keyType;
        inData[1] = blockCnt;
        inData[2] = blockAddr;
        System.arraycopy(key, 0, inData, 3, 6);
        System.arraycopy(data, 0, inData, 9, 16 * blockCnt);
        return this.optFunction((byte) 0, (byte) 33, inData, (byte) inData.length, outData, outLen, errCode);
    }

    public int Iso14443a_GetUid(byte[] uid, byte[] uidLen, byte[] errCode) {
        byte[] inData = new byte[]{38, 0};
        byte[] outData = new byte[256];
        byte[] outLen = new byte[1];
        int result = this.optFunction((byte) 0, (byte) 37, inData, (byte) inData.length, outData, outLen, errCode);
        if (result == 0) {
            uidLen[0] = (byte) (outLen[0] - 1);
            System.arraycopy(outData, 1, uid, 0, uidLen[0]);
        }

        return result;
    }

    public int Iso14443a_GetUid_M2(byte[] uid) {
        int result = this.optFunction_M2 (uid);
        return result;
    }

    public int Iso14443a_Rats(byte[] Ats, byte[] AtsLen, byte[] errCode) {
        byte[] inData = new byte[]{38, 0};
        return this.optFunction((byte) 0, (byte) 39, inData, (byte) inData.length, Ats, AtsLen, errCode);
    }

    public int Iso14443a_Apdu(byte[] wData, byte wLen, byte[] rData, byte[] rLen, byte[] errCode) {
        byte[] inData = new byte[wLen + 2];
        byte[] outData = new byte[256];
        byte[] outLen = new byte[1];
        inData[0] = 0;
        inData[1] = wLen;
        System.arraycopy(wData, 0, inData, 2, wLen);
        int result = this.optFunction((byte) 0, (byte) 40, inData, (byte) inData.length, outData, outLen, errCode);
        if (result == 0) {
            rLen[0] = outLen[0];
            System.arraycopy(outData, 0, rData, 0, rLen[0]);
        }

        return result;
    }

    public int Iso14443b_GetUid(byte[] pupi, byte[] pupiLen, byte[] errCode) {
        byte[] outData = new byte[256];
        byte[] outLen = new byte[1];
        int result = this.optFunction((byte) 0, (byte) 9, (byte[]) null, (byte) 0, outData, outLen, errCode);
        if (result == 0) {
            pupiLen[0] = outLen[0];
            System.arraycopy(outData, 1, pupi, 0, pupiLen[0]);
        }

        return result;
    }

    public int Iso14443b_Apdu(byte[] wData, byte wLen, byte[] rData, byte[] rLen, byte[] errCode) {
        byte[] inData = new byte[wLen + 1];
        byte[] outData = new byte[256];
        byte[] outLen = new byte[1];
        inData[0] = wLen;
        System.arraycopy(wData, 0, inData, 1, wLen);
        int result = this.optFunction((byte) 0, (byte) 13, inData, (byte) inData.length, outData, outLen, errCode);
        if (result == 0) {
            rLen[0] = outLen[0];
            System.arraycopy(outData, 0, rData, 0, rLen[0]);
        }

        return result;
    }

    public int Iso15693_Inventory(byte flags, byte afi, byte[] numOfCard, byte[] outFlag, byte[] dsfid, byte[] uid, byte[] uidLen, byte[] errCode) {
        byte[] inData = new byte[3];
        byte[] outData = new byte[256];
        byte[] outLen = new byte[1];
        inData[0] = flags;
        inData[1] = afi;
        inData[2] = 0;
        int result = this.optFunction((byte) 0, (byte) 16, inData, (byte) inData.length, outData, outLen, errCode);
        if (result == 0) {
            numOfCard[0] = outData[0];
            outFlag[0] = outData[1];
            dsfid[0] = outData[2];
            uidLen[0] = (byte) (outLen[0] - 3);
            System.arraycopy(outData, 1, uid, 0, uidLen[0]);
        }

        return result;
    }

    public int Iso15693_ReadBlock(byte flags, byte startBlk, byte numOfBlk, byte[] uid, byte[] outFlag, byte[] data, byte[] dataLen, byte[] errCode) {
        byte[] inData = new byte[256];
        byte[] outData = new byte[256];
        byte[] outLen = new byte[1];
        inData[0] = flags;
        inData[1] = startBlk;
        inData[2] = numOfBlk;
        int result;
        if (uid != null) {
            System.arraycopy(uid, 0, inData, 3, 8);
            result = this.optFunction((byte) 0, (byte) 17, inData, (byte) 11, outData, outLen, errCode);
        } else {
            result = this.optFunction((byte) 0, (byte) 17, inData, (byte) 3, outData, outLen, errCode);
        }

        if (result == 0) {
            outFlag[0] = outData[0];
            dataLen[0] = (byte) (outLen[0] - 1);
            System.arraycopy(outData, 1, data, 0, dataLen[0]);
        }

        return result;
    }

    public int Iso15693_WriteBlock(byte flags, byte startBlk, byte numOfBlk, byte[] uid, byte[] data, byte dataLen, byte[] errCode) {
        byte[] inData = new byte[256];
        byte[] outData = new byte[256];
        byte[] outLen = new byte[1];
        inData[0] = flags;
        inData[1] = startBlk;
        inData[2] = numOfBlk;
        int result;
        if (uid != null) {
            System.arraycopy(uid, 0, inData, 3, 8);
            System.arraycopy(data, 0, inData, 11, dataLen);
            result = this.optFunction((byte) 0, (byte) 18, inData, (byte) (11 + dataLen), outData, outLen, errCode);
        } else {
            System.arraycopy(data, 0, inData, 3, dataLen);
            result = this.optFunction((byte) 0, (byte) 18, inData, (byte) (3 + dataLen), outData, outLen, errCode);
        }

        return result;
    }

    public int Iso15693_LockBlock(byte flags, byte blkAddr, byte[] uid, byte[] errCode) {
        byte[] inData = new byte[256];
        byte[] outData = new byte[256];
        byte[] outLen = new byte[1];
        inData[0] = flags;
        inData[1] = blkAddr;
        int result;
        if (uid != null) {
            System.arraycopy(uid, 0, inData, 2, 8);
            result = this.optFunction((byte) 0, (byte) 18, inData, (byte) 10, outData, outLen, errCode);
        } else {
            result = this.optFunction((byte) 0, (byte) 19, inData, (byte) 2, outData, outLen, errCode);
        }

        return result;
    }
}
