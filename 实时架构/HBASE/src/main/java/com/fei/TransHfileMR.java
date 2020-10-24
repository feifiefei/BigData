package com.itheima;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;

import java.io.IOException;

/**
 * Author: 飞
 * Date: 2020/9/23 0023 20:25
 * FileName: TransHfileMR
 * Description: todo :把一个普通文件的数据转换为一个HFILE的数据文件
 */
public class TransHfileMR extends Configured implements Tool {


    public int run(String[] args) throws Exception {
        //todo 1：构建一个job
        Job job = Job.getInstance(this.getConf(), "toHfile");
        job.setJarByClass(TransHfileMR.class);
        //todo 2:配置job
        //input：读取一个普通文件
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        //todo 3:map:将普通文件中的数据封装成put对象，用于构建HFILE文件
        job.setMapperClass(ToHfileMap.class);
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);
        job.setMapOutputValueClass(Put.class);
        //todo 4:reduce
        job.setNumReduceTasks(0);
        //todo 5:output 将输出的数据编程hfile文件
        job.setOutputFormatClass(HFileOutputFormat2.class);
        //todo 6:将生成的HFILE文件保存在什么位置
        HFileOutputFormat2.setOutputPath(job, new Path(args[1]));
        //todo 7:需要以下配置，将put转化对应的cell
        Connection conn = ConnectionFactory.createConnection(this.getConf());
        //定义最终该hfile要导入哪张表
        Table table = conn.getTable(TableName.valueOf("mrhbase"));
        //获取该表对应的region的位置，用于将文件放入对应的region中
        RegionLocator regionLocator = conn.getRegionLocator(TableName.valueOf("mrhbase"));
        //获取该表对应的region的位置，用于将文件放入对应的region中
        regionLocator = conn.getRegionLocator(TableName.valueOf("mrhbase"));
        //指定将该hfile文件最终导入这张表的这个region中
        HFileOutputFormat2.configureIncrementalLoad(job, table, regionLocator);
        //submit
        return job.waitForCompletion(true) ? 0 : -1;
    }

    public static class ToHfileMap extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {
        private ImmutableBytesWritable outputKey = new ImmutableBytesWritable();
        private byte[] family = Bytes.toBytes("info");

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException, IOException {
            String line = value.toString();
            //将这一行内容进行分割
            String[] items = line.split("\t");
            //将第一列作为rowkey
            String rowkey = items[0];
            this.outputKey.set(rowkey.getBytes());
            Put outputValue = new Put(this.outputKey.get());
            //将名字这一列放入put对象
            String name = items[1];
            outputValue.addColumn(family, "name".getBytes(), name.getBytes());
            context.write(this.outputKey, outputValue);
            //将年龄这一列放入put对象
            String age = items[2];
            outputValue.addColumn(family, "age".getBytes(), age.getBytes());
            context.write(this.outputKey, outputValue);
            //将性别这一列放入put对象
            String sex = items[3];
            outputValue.addColumn(family, "sex".getBytes(), sex.getBytes());
            context.write(this.outputKey, outputValue);
        }
    }


}
