package com.example.lp.ddncredit.utils;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.API_TUOYUBAO_BASE;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.API_XIAONUO_BASE;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.NAME_TUOYUBAO_BASE;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.NAME_XIAONUO_BASE;

/**
 * Created by lochy on 15/5/12.
 */
public class StringTool {


    /**
     * 通过uid转换为RFid
     *
     * @param data
     * @return
     */
    public static BigInteger getRfid(String data) {
        StringBuffer stringBuffer = new StringBuffer();

        /*hd,高低位转换*/
        for (int i = 0; i < data.length(); ) {
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
     * 快速的转换byte为16进制字符串
     */
    public static String byteArrayToHexStr(byte[] byteArray, int bytelength) {
        if (byteArray == null) {
            return null;
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytelength * 2];
        for (int j = 0; j < bytelength; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        String hexStr = new String(hexChars);
        return hexStr;
    }
    /**
     * 高低算法
     */
    public static String hdHex(String hex, int lenght) {
        String hexex[] = new String[lenght / 2];
        String newHex = "";
        //8057204A
        //4A205780
        for (int i = 0; i < hex.length() / 2; i++) {
            String temstr = hex.substring(hex.length() - 2 - i * 2, hex.length() - i * 2);
            hexex[i] = temstr;
//            System.out.println("temstr  "+temstr);
        }
        for (String s : hexex) {
            newHex = newHex + s;
        }
//        System.out.println( "newHex: "+newHex);
        return newHex;

    }
    /**
     * 高低位转换另一种方法
     * 该方法高低转换效率更高，速度更快
     */
    public static String reverseHdHex(String hex) {
        final char[] charArray = hex.toCharArray();
        final int length = charArray.length;
        final int times = length / 2;
        for (int c1i = 0; c1i < times; c1i += 2) {
            final int c2i = c1i + 1;
            final char c1 = charArray[c1i];
            final char c2 = charArray[c2i];
            final int c3i = length - c1i - 2;
            final int c4i = length - c1i - 1;
            charArray[c1i] = charArray[c3i];
            charArray[c2i] = charArray[c4i];
            charArray[c3i] = c1;
            charArray[c4i] = c2;
        }
        return new String(charArray);
    }

    public static boolean isHexStrValid(String str) {
        String pattern = "^[0-9A-F]+$";
        return Pattern.compile(pattern).matcher(str).matches();
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
        for (int i = 0; i < urlString.length(); ) {
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


    /**
     * 将可见地址隐藏
     */
    public static String hideKeyAdress(String adress) {
        String reuslt = adress;
        /*
         *将可见地址隐藏
         * */
        if (adress.equals(API_TUOYUBAO_BASE)) {
            reuslt = NAME_TUOYUBAO_BASE;
        } else if (adress.equals(API_XIAONUO_BASE)) {
            reuslt = NAME_XIAONUO_BASE;
        }
        return reuslt;
    }

    /**
     * 将隐藏地址显示
     */
    public static String showKeyAdress(String adress) {
        String reuslt = adress;
        /*
         * 将隐藏地址显示
         * */
        if (adress.equals(NAME_TUOYUBAO_BASE)) {
            reuslt = API_TUOYUBAO_BASE;
        } else if (adress.equals(NAME_XIAONUO_BASE)) {
            reuslt = API_XIAONUO_BASE;
        } else if (adress.length() < 1) {//如果位数小于1，则表示在本地没有读取到
            reuslt = API_TUOYUBAO_BASE;//则默认为托育宝幼儿园
        }
        return reuslt;
    }

    /**
     * 判断是否为网址
     */
    public static boolean isHttpUrl(String urls) {
        boolean isurl = false;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式

        String regex2 = "[a-zA-z]+://[^\\s]*";
        Pattern pat = Pattern.compile(regex2.trim());//对比
        Matcher mat = pat.matcher(urls.trim());
        isurl = mat.matches();//判断是否匹配
        if (isurl) {
            isurl = true;
        }
        return isurl;
    }

}
