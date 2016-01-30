import java.io.*;

/**
 * Created by Administrator on 2014/11/17.
 */
public class MergeSql {

    public static void main(String[] args) throws IOException {
        String filePath = "C:/Users/Administrator/Desktop/sg_project/view";
        String targetPath = "D:/target.view.11.16.sql";
        File targetFile = new File(targetPath);
        targetFile.createNewFile();
        OutputStream out = new FileOutputStream(targetFile);
        File file = new File(filePath);
        File[] files = file.listFiles();
        for(File subfile : files){
            try {
                InputStream in = new FileInputStream(subfile);
                int fileLength = (int)subfile.length();
                byte[] bytes = new byte[fileLength];
                in.read(bytes);
                in.close();
                out.write(bytes);
                out.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        out.close();
    }
}
