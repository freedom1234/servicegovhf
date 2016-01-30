package com.dc.esb.servicegov.export.util;

import com.dc.esb.servicegov.entity.SDA;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by Administrator on 2015/7/16.
 */

@Component
public class FileUtil {

    protected static Log logger = LogFactory.getLog(FileUtil.class);

    public static String readFile(File file) {
        if (logger.isInfoEnabled()) {
            logger.info("开始读取文件[" + file.getAbsolutePath() + "]");
        }
        int fileLength = (int) file.length();
        DataInputStream dataIn = null;
        String fileContent = null;
        try {
            dataIn = new DataInputStream(new FileInputStream(file));
            byte[] fileBytes = new byte[fileLength];
            dataIn.readFully(fileBytes);
            fileContent = new String(fileBytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != dataIn) {
                try {
                    dataIn.close();
                } catch (IOException e) {
                    logger.error(e, e);
                }
            }
        }
        return fileContent;
    }

    public static void writeFile(String fileContent, String filePath) {
        File file = new File(filePath);
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(file);
            fileOut.write(fileContent.getBytes());
        } catch (FileNotFoundException e) {
            logger.error(e, e);
        } catch (IOException e) {
            logger.error(e, e);
        }
    }

    public static void copyFile(String srcFile, String destFile, String requestTxt, String responseText) throws Exception {
        logger.info("复制文件开始，srcFile:" + srcFile + ",destFile:" + destFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(srcFile)),
                "utf-8"));
        File dFile = new File(destFile);
        if (!dFile.isFile()) {
            dFile.getParentFile().mkdirs();
            dFile.createNewFile();
        }


        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(destFile)),
                "utf-8"));

        String temp = null;
        while ((temp = reader.readLine()) != null) {
            String body = temp;
            if (temp.indexOf("${request}$") != -1) {
                body = temp.replace("${request}$", requestTxt);
            } else if (temp.indexOf("${response}$") != -1) {
                body = temp.replace("${response}$", responseText);
            }
            writer.write(body);
            writer.newLine();
        }

        writer.close();
        if (writer != null) {
            writer.close();
            writer = null;
        }
        if (reader != null) {
            reader.close();
            reader = null;
        }

        //格式化xml
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(destFile);
        FileOutputStream out = new FileOutputStream(destFile, false);
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        XMLWriter formatWriter = new XMLWriter(new OutputStreamWriter(out, "utf-8"), format);// 重新写回到原来的xml文件中

        formatWriter.write(document);

        formatWriter.close();
        out.close();


        logger.info("复制结束...");
    }


    public static void copyFile(String srcFile, String destFile, String requestTxt, String responseText, String reqHeadTxt, String rspHeadTxt) throws Exception {
        logger.info("复制文件开始，srcFile:" + srcFile + ",destFile:" + destFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(srcFile)),
                "utf-8"));
        File dFile = new File(destFile);
        if (!dFile.isFile()) {
            dFile.getParentFile().mkdirs();
            dFile.createNewFile();
        }


        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(destFile)),
                "utf-8"));

        String temp = null;
        while ((temp = reader.readLine()) != null) {
            String body = temp;
            if (temp.indexOf("${request}$") != -1) {
                body = temp.replace("${request}$", requestTxt);
            } else if (temp.indexOf("${response}$") != -1) {
                body = temp.replace("${response}$", responseText);
            } else if (temp.indexOf("${reqHead}$") != -1) {
                if (null == reqHeadTxt) {
                    reqHeadTxt = "";
                }
                body = temp.replace("${reqHead}$", reqHeadTxt);

            } else if (temp.indexOf("${rspHead}$") != -1) {
                if (null == rspHeadTxt) {
                    rspHeadTxt = "";
                }
                body = temp.replace("${rspHead}$", rspHeadTxt);
            }
            writer.write(body);
            writer.newLine();
        }

        writer.close();
        if (writer != null) {
            writer.close();
            writer = null;
        }
        if (reader != null) {
            reader.close();
            reader = null;
        }

        //格式化xml
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(destFile);
        FileOutputStream out = new FileOutputStream(destFile, false);
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        XMLWriter formatWriter = new XMLWriter(new OutputStreamWriter(out, "utf-8"), format);// 重新写回到原来的xml文件中

        formatWriter.write(document);

        formatWriter.close();
        out.close();


        logger.info("复制结束...");
    }

    public static void copyFileTZBIdentifyOut(String srcFile, String destFile, String systemAb, String identifySystemOutText) throws Exception {
        logger.info("复制文件开始，srcFile:" + srcFile + ",destFile:" + destFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(srcFile)),
                "utf-8"));
        File dFile = new File(destFile);
        if (!dFile.isFile()) {
            dFile.getParentFile().mkdirs();
            dFile.createNewFile();
        }

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(destFile)),
                "utf-8"));

        String temp = null;
        while ((temp = reader.readLine()) != null) {
            String body = temp;
            if (temp.indexOf("${systemAb}$") != -1) {
                body = body.replace("${systemAb}$", systemAb);
            }
            if (temp.indexOf("${outText}$") != -1) {
                body = body.replace("${outText}$", identifySystemOutText);
            }
            writer.write(body);
            writer.newLine();
        }

        writer.close();
        if (writer != null) {
            writer.close();
            writer = null;
        }
        if (reader != null) {
            reader.close();
            reader = null;
        }

        //格式化xml
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(destFile);
        FileOutputStream out = new FileOutputStream(destFile, false);
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        XMLWriter formatWriter = new XMLWriter(new OutputStreamWriter(out, "utf-8"), format);// 重新写回到原来的xml文件中

        formatWriter.write(document);

        formatWriter.close();
        out.close();


        logger.info("复制结束...");
    }

    public static void copyFileTZBIdentify(String srcFile, String destFile, String systemAb, String identifySystemInText) throws Exception {
        logger.info("复制文件开始，srcFile:" + srcFile + ",destFile:" + destFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(srcFile)),
                "utf-8"));
        File dFile = new File(destFile);
        if (!dFile.isFile()) {
            dFile.getParentFile().mkdirs();
            dFile.createNewFile();
        }

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(destFile)),
                "utf-8"));

        String temp = null;
        while ((temp = reader.readLine()) != null) {
            String body = temp;
            if (temp.indexOf("${systemAb}$") != -1) {
                body = body.replace("${systemAb}$", systemAb);
            }
            if (temp.indexOf("${inText}$") != -1) {
                body = body.replace("${inText}$", identifySystemInText);
            }
            writer.write(body);
            writer.newLine();
        }

        writer.close();
        if (writer != null) {
            writer.close();
            writer = null;
        }
        if (reader != null) {
            reader.close();
            reader = null;
        }

        //格式化xml
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(destFile);
        FileOutputStream out = new FileOutputStream(destFile, false);
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        XMLWriter formatWriter = new XMLWriter(new OutputStreamWriter(out, "utf-8"), format);// 重新写回到原来的xml文件中

        formatWriter.write(document);

        formatWriter.close();
        out.close();


        logger.info("复制结束...");
    }

    public static void copyFileTZB(String srcFile, String destFile, String systemAb, String ecode, String requestTxt, String responseText) throws Exception {
        logger.info("复制文件开始，srcFile:" + srcFile + ",destFile:" + destFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(srcFile)),
                "utf-8"));
        File dFile = new File(destFile);
        if (!dFile.isFile()) {
            dFile.getParentFile().mkdirs();
            dFile.createNewFile();
        }

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(destFile)),
                "utf-8"));

        SDA SDARequest = null;
        SDA SDAResponse = null;

        String temp = null;
        while ((temp = reader.readLine()) != null) {
            String body = temp;
            if (temp.indexOf("${ecode}$") != -1) {
                body = body.replace("${ecode}$", ecode);
            }
            if (temp.indexOf("${systemAb}$") != -1) {
                body = body.replace("${systemAb}$", systemAb);
            }
            if (temp.indexOf("${request}$") != -1) {
                body = body.replace("${request}$", requestTxt);
            }
            if (temp.indexOf("${response}$") != -1) {
                body = body.replace("${response}$", responseText);
            }
            writer.write(body);
            writer.newLine();
        }

        writer.close();
        if (writer != null) {
            writer.close();
            writer = null;
        }
        if (reader != null) {
            reader.close();
            reader = null;
        }

        //格式化xml
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(destFile);
        FileOutputStream out = new FileOutputStream(destFile, false);
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        XMLWriter formatWriter = new XMLWriter(new OutputStreamWriter(out, "utf-8"), format);// 重新写回到原来的xml文件中

        formatWriter.write(document);

        formatWriter.close();
        out.close();


        logger.info("复制结束...");
    }

    /**
     * 递归删除目录（文件夹）以及目录下的文件
     *
     * @param dirFile 删除的目录
     * @return 目录删除成功返回true，否则返回false
     * @throws Exception 异常
     */
    public static boolean deleteDirectory(File dirFile) throws Exception {
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (null == dirFile || !dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            return false;
        }
        // 删除当前目录
        boolean dele = dirFile.delete();
        return dele;
    }

    /**
     * 删除目录
     *
     * @param sPath 目录路径
     * @return 删除是否成功
     * @throws Exception 异常
     */
    public static boolean deleteDirectory(String sPath) throws Exception {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        return deleteDirectory(new File(sPath));
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     * @throws Exception 异常
     */
    public static boolean deleteFile(String sPath) throws Exception {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            flag = file.delete();
            if (flag) {
                logger.info("文件：" + file.getAbsolutePath() + "删除成功");
            }
        }
        return flag;
    }


}
