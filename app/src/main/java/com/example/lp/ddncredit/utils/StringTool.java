package com.example.lp.ddncredit.utils;

import java.math.BigInteger;

/**
 * Created by lochy on 15/5/12.
 */
public class StringTool {


    /**
     * 通过uid转换为RFid
     * @param data
     * @return
     */
    public static BigInteger getRfid(String data) {
        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < data.length();) {
            int end = i + 2;
            stringBuffer.insert(0, data.substring(i, end));
            i = end;
        }
        String s = new String(stringBuffer);
        //System.out.println(Integer.parseInt(s, 16));
        BigInteger bigInteger = new BigInteger(s.trim(), 16);
        return bigInteger;
    }
    public static String byteHexToSting(byte[] data) {
        if (data == null) {
            return null;
        }
        StringBuilder stringBuffer = new StringBuilder();
        for (int aR_data : data) {
            //            stringBuffer.append(Integer.toHexString(aR_data & 0x00ff));
            stringBuffer.append(String.format("%02x", aR_data & 0x00ff));
        }
        return stringBuffer.toString();
    }

    /**
     * byte[]转变为16进制String字符, 每个字节2位, 不足补0
     */
    public static String getStringByBytes(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        String result = null;
        String hex = null;
        if (bytes != null && bytes.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(bytes.length);
            for (byte byteChar : bytes) {
                hex = Integer.toHexString(byteChar & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                stringBuilder.append(hex.toUpperCase());
            }
            result = stringBuilder.toString();
        }
        return result;
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte[] urlStringToBytes(String urlString) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i<urlString.length(); ) {
            if (urlString.charAt(i) == '%') {
                if ((i + 2) < urlString.length()) {
                    stringBuilder.append(urlString.substring(i + 1, i + 3));
                }
                i += 3;
            } else {
                stringBuilder.append(String.format("%02x", urlString.charAt(i) & 0xff));
                i++;
            }
        }

        return hexStringToBytes(stringBuilder.toString());
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEFabcdef".indexOf(c);
    }
}
