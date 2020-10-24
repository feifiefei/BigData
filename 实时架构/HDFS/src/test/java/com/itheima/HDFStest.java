package com.itheima;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * Author: 飞
 * Date: 2020/8/21 0021 19:54
 * FileName: HDFStest
 * Description: HDFS功能测试
 */
public class HDFStest {

    public FileSystem getHDFSConnect() throws IOException, URISyntaxException {
        //todo:创建HDFS连接
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://node1:8020"), conf);
        return fs;
    }

    //todo:创建目录
    @Test
    public void createTest() throws IOException, URISyntaxException {
        FileSystem fs = getHDFSConnect();
        Path path = new Path("/windows/input/test");
        if (fs.exists(path)) {
            fs.delete(path, true);
        }
        fs.mkdirs(path);
        fs.close();
    }

    //todo:上传本地文件
    @Test
    public void iploadToHDFS() throws IOException, URISyntaxException {
        FileSystem fs = getHDFSConnect();
        Path loadpath = new Path("file:///F:/test/瞎求写.txt");
        Path path = new Path("/windows/input/test");
        fs.copyFromLocalFile(loadpath, path);
        fs.close();
    }

    //todo:下载
    @Test
    public void downtoHDFS() throws IOException, URISyntaxException {
        FileSystem fs = getHDFSConnect();
        fs.copyToLocalFile(new Path("/windows/input/test/瞎求写.txt"), new Path("file:///F:"));
        fs.close();
    }


    //todo：上传多个文件合并成一个
    @Test
    public void mergeupToHDFS() throws IOException, URISyntaxException {
        FileSystem fs = getHDFSConnect();
        //创建输出流
        FSDataOutputStream fsDataOutputStream = fs.create(new Path("/windows/input/test/merge.txt"));
        //创建输入流
        //
        LocalFileSystem local = FileSystem.getLocal(new Configuration());
        FileStatus[] fileStatuses = local.listStatus(new Path("F:\\test"));
        for (FileStatus fileStatus : fileStatuses) {
            FSDataInputStream fsIS = local.open(fileStatus.getPath());
            IOUtils.copyBytes(fsIS, fsDataOutputStream, 4096);
            fsIS.close();
        }
        fs.close();
    }

}
