package com.dc.esb.servicegov;

/**
 * Created by vincentfxz on 15/7/14.
 */
public class TestBytesToHexString {

    public static void main(String[] args){

        int length = 517;
        System.out.println(Integer.toHexString(517));
        byte[] header = new byte[2];
        header[1] = (byte)(length & 0xff);
        header[0] = (byte)(length >> 8 & 0xff);
        System.out.println(bytesToHex(header));

        length = 0;



        int highvalue = header[0] & 0xff;
        highvalue = highvalue * 256;
        int lowValue = header[1] &0xff;
        length = highvalue + lowValue;
        System.out.println(length);






    }

    public static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
