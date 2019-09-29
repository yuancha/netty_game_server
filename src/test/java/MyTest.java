import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyTest {

    private static final Logger log = LoggerFactory.getLogger(MyTest.class);

    public static void main(String[] args) {
        final byte[] result = new byte[2];
        short msgType = 10;
        result[0] = (byte) (msgType >>> 8);
        result[1] = (byte) msgType;
        System.out.println(result[0]);
        System.out.println(result[1]);
        log.debug("1111");
    }
}
