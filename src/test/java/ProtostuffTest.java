import pojo.Role;
import util.ProtostuffUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ProtostuffTest {
    public static void main(String[] args) {
        Role role = new Role();
        role.setId(1);
        role.setNick("a");
        role.setSex(0);
//        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
//             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
//            oos.writeObject(role);
//            byte[] bytes = baos.toByteArray();
//            System.out.println(bytes.length);
//        } catch (IOException e) {
//            System.out.println(e);
//        }

        byte[] bytes = ProtostuffUtil.serialize(role);
        System.out.println("序列化成功");

        Role role1 = ProtostuffUtil.deserialize(bytes, Role.class);
        System.out.println(role);
    }
}
