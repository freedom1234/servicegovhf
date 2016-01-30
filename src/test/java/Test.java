import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by kongxfa on 2016/1/28.
 */
public class Test {
    @org.junit.Test
    public void test(){
//        String Id="%E6%B5%8B%E8%AF%95";
//        String Id="æµ\u008Bè¯";
        String Id="测试";
        try {
//            Id = URLDecoder.decode(Id, "UTF-8");
            Id = java.net.URLDecoder.decode(Id, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("+++++++++++++++++解码后:"+Id);
    }


}
