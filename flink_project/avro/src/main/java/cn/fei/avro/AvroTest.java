package cn.fei.avro;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;

/**
 * @description:todo: 演示avro的序列化和反序列化
 * @author: 飞
 * @date: 2020/10/28 0028 19:28
 */
public class AvroTest {
    /**
     * 演示序列化和反序列化
     * 开发步骤：
     * 1.新建对象
     * 2.封装数据
     * 3.序列化
     * 4.反序列
     */
    public static void main(String[] args) throws IOException {
        //新建对象
        User user = new User();
        //封装数据
        user.setName("唐三");
        user.setAge(20);
        user.setAddress("圣魂村");

        User user2 = new User("小舞", 18, "星斗大森林");
        User user3 = User.newBuilder()
                .setName("胡烈那")
                .setAge(18)
                .setAddress("武魂殿")
                .build();
        //序列化
//        //定义模式,设置schema
//        //SpecificDatumWriter<User> userSpecificDatumWriter = new SpecificDatumWriter<>(user.getSchema());//方式一
//        SpecificDatumWriter<User> userSpecificDatumWriter1 = new SpecificDatumWriter<>(User.class);//方式二
//        //数据序列化对象
//        DataFileWriter<User> fileWriter = new DataFileWriter<>(userSpecificDatumWriter1);
//        //设置序列化文件路径
//        fileWriter.create(user.getSchema(), new File("avro.txt"));
//        //写数据
//        fileWriter.append(user);
//        fileWriter.append(user2);
//        fileWriter.append(user3);
//        //关流
//        fileWriter.close();
        //反序列
        //定义反序列schema
        SpecificDatumReader<User> userSpecificDatumReader = new SpecificDatumReader<>(User.class);
        DataFileReader<User> users = new DataFileReader<>(new File("avro.txt"), userSpecificDatumReader);
        for (User user1 : users) {
            System.out.println("反序列化" + user1);
        }
        //关流
        users.close();

    }
}
