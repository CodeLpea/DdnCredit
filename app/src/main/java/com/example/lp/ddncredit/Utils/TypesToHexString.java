package com.example.lp.ddncredit.Utils;

/**
 * Created by Long on 2018/1/25.
 */

public class TypesToHexString {
    public TypesToHexString(){

    }

    public static String byte2hex(byte [] buffer){
        String h = "";
        for(int i = 0; i < buffer.length; i++){
            String temp = Integer.toHexString(buffer[i] & 0xFF);
            if(temp.length() == 1){
                temp = "0" + temp;
            }
            h = h + ", "+ "0x" + temp;
        }
        return h;
    }

    public static String char2hex(char [] buffer){
        String h = "";
        for(int i = 0; i < buffer.length; i++){
            String temp = Integer.toHexString(buffer[i] & 0xFFFF);
            if(temp.length() == 1){
                temp = "000" + temp;
            }else if(temp.length() == 2){
                temp = "00" + temp;
            }
            else if(temp.length() == 3){
                temp = "0" + temp;
            }
            h = h + ", "+ "0x" + temp;
        }
        return h;
    }

    public static String short2hex(short[] buffer){
        String h = "";
        for(int i = 0; i < buffer.length; i++){
            String temp = Integer.toHexString(buffer[i] & 0xFFFF);
            if(temp.length() == 1){
                temp = "000" + temp;
            }else if(temp.length() == 2){
                temp = "00" + temp;
            }
            else if(temp.length() == 3){
                temp = "0" + temp;
            }
            h = h + ", "+ "0x" + temp;
        }
        return h;
    }

}
