import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ByteBufTest {


    public static void main(String[] args) {
        ByteBufTest test = new ByteBufTest();
        test.test2();
    }

    public void test1() {
        ByteBuf buf = Unpooled.buffer(10);
        byte[] bytes = {1,2,3,4,5};
        buf.writeBytes(bytes);
        System.out.println(Arrays.toString(buf.array()));
        buf.readInt();
        System.out.println(buf.arrayOffset() + buf.readerIndex());
    }

    public void test2() {
        int i = 1000;
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        DataOutputStream dos = new DataOutputStream(baos);
//        try {
//            dos.write(num);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(Arrays.toString(baos.toByteArray()));
        byte[] result = new byte[4];
        // 由高位到低位
        result[0] = (byte) ((i >>> 24));
        result[1] = (byte) ((i >>> 16));
        result[2] = (byte) ((i >>> 8));
        result[3] = (byte) (i);
        System.out.println(Arrays.toString(result));
    }
}
