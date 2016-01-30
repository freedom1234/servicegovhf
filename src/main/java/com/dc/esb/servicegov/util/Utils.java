package com.dc.esb.servicegov.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public final static String currentCount = "并发数";
    public final static String timeOut = "超时时间";
    public final static String averageTime = "平均响应时间";
    public final static String successRate = "业务成功率";

    public static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * get data type length scale
     *
     * @param value
     * @return
     */
    public static String getDataType(String value) {
        String type = null;
        if (value != null && !"".equals(value)) {
            if (value.contains("(")) {
                type = value.substring(0, value.indexOf("(")).toLowerCase();
            } else {
                type = value;
            }
        }
        return type;
    }

    public static String getDataLength(String value) {
        String length = null;
        if (value != null && !"".equals(value)) {
            if (value.indexOf("(") > 0) {
                if (value.indexOf(",") > 0) {
                    length = value.substring(value.indexOf("(") + 1, value
                            .indexOf(","));
                } else {
                    length = value.substring(value.indexOf("(") + 1, value
                            .length() - 1);
                }
            }
        }
        return length;
    }

    public static String getDataScale(String value) {
        String scale = null;
        if (value != null && !"".equals(value)) {
            if (value.indexOf(",") > 0) {
                scale = value.substring(value.indexOf(",") + 1,
                        value.length() - 1);
            }
        }
        return scale;
    }

    public static String getDataLengthFromLength(String lengthValue){
        String length = "";
        if (lengthValue.indexOf(",") > 0) {
            length = lengthValue.substring(0, lengthValue
                    .indexOf(","));
        } else {
            length = lengthValue;
        }
        return length;
    }

    public static String getDataScaleFromLength(String lengthValue){
        String scale = "";
        if (lengthValue.indexOf(",") > 0) {
            scale = lengthValue.substring(lengthValue.indexOf(",") + 1,
                    lengthValue.length());
        }
        return scale;
    }


    // 基础版本号，修改
    public static String modifyversionno(String versionno) {
        String[] num = versionno.split("\\.");
        num[2] = (Integer.parseInt(num[2]) + 1) + "";
        versionno = num[0] + "." + num[1] + "." + num[2];
        return versionno;
    }

    // 基础版本号，修改
    public static String modifyOnlineNo(String newOnlineVersion, String versionno) {
        String[] num = versionno.split("\\.");
        versionno = newOnlineVersion + "." + num[1] + "." + num[2];
        return versionno;
    }

    //将byte
    public static String byte2MGT(String byteString) {
        double temp;
        double temp1;
        double t2;
        double t;
        String tt;

        BigInteger by = new BigInteger(byteString);
        if (by.doubleValue() == new BigInteger("0").doubleValue()) {
            tt = String.valueOf(0 + "B");
        }
        else if (by.doubleValue() < new BigInteger("1024").doubleValue()
                && by.doubleValue() > new BigInteger("0").doubleValue()) {
            int t3 = (int) Math.round(by.doubleValue());
            tt = String.valueOf(t3 + "B");
        }
        else if (by.doubleValue() < new BigInteger("1048576").doubleValue()
                && by.doubleValue() >= new BigInteger("1024").doubleValue()) {
            temp = by.doubleValue() / 1024;
            t = Math.round(temp * 10);
            t2 = t / 10.0;
            tt = String.valueOf(t2 + "KB");
        }
        else if (by.doubleValue() < new BigInteger("1073741824").doubleValue()
                && by.doubleValue() >= new BigInteger("1048576").doubleValue()) {
            temp1 = by.doubleValue() / 1024 / 1024;
            t = Math.round(temp1 * 10);
            t2 = t / 10.0;
            tt = String.valueOf(t2 + "M");
        }
        else if (by.doubleValue() < new BigInteger("1099511627776").doubleValue()
                && by.doubleValue() >= new BigInteger("1073741824").doubleValue()) {
            temp1 = by.doubleValue() / 1024 / 1024 / 1024;
            t = Math.round(temp1 * 10);
            t2 = t / 10;
            tt = String.valueOf(t2 + "G");
        }
        else {
            temp1 = by.doubleValue() / 1024 / 1024 / 1024 / 1024;
            t = Math.round(temp1 * 10);
            t2 = t / 10;
            tt = String.valueOf(t2 + "T");
        }
        return tt;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.println(modifyversionno("1.0.0"));
    }

}