package cn.fei.avro;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.specific.SpecificDatumReader;

import java.io.File;
import java.io.IOException;

/**
 * @Date 2020/10/28
 */
public class AvroTest {

    public static void main(String[] args) throws IOException {

        /**
         * 演示序列化和反序列化
         * 开发步骤：
         * 1.新建对象
         * 2.封装数据
         * 3.序列化
         * 4.反序列
         */
        //1.新建对象
        User user = new User();

        //2.封装数据
        user.setName("唐三");
        user.setAge(20);
        user.setAddress("圣魂村");

        User user2 = new User("小舞", 18, "星斗大森林");

        User user3 = User.newBuilder()
                .setName("胡烈那")
                .setAge(18)
                .setAddress("武魂殿")
                .build();

//        //3.序列化
//        //定义模式，设置schema
//        SpecificDatumWriter<User> userSpecificDatumWriter = new SpecificDatumWriter<>(user.getSchema());
//        //SpecificDatumWriter<User> userSpecificDatumWriter1 = new SpecificDatumWriter<>(User.class);
//        //数据序列化对象
//        DataFileWriter<User> dataFileWriter = new DataFileWriter<>(userSpecificDatumWriter);
//        //设置序列化文件路径
//        dataFileWriter.create(user.getSchema(),new File("avro.txt"));
//        //写数据
//        dataFileWriter.append(user);
//        dataFileWriter.append(user2);
//        dataFileWriter.append(user3);
//        //关流
//        dataFileWriter.close();

        // 4.反序列
        //定义反序列化schema
        SpecificDatumReader<User> datumReader = new SpecificDatumReader<>(User.class);
        DataFileReader<User> fileReader = new DataFileReader<>(new File("avro.txt"), datumReader);
        for (User user4 : fileReader) {
            System.out.println("反序列化："+user4);
        }

        //关流
        fileReader.close();
    }
}
