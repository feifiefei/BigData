package com.itheima.secondOrder;

import com.itheima.count.countmain;
import com.itheima.count.countphone;
import com.sun.jersey.spi.inject.ConstrainedTo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Comparator;

/**
 * Author: 飞
 * Date: 2020/8/24 0024 21:13
 * FileName: secondOrder
 * Description: 对文件进行二次排序
 */
public class secondOrder extends Configured implements Tool {

    public int run(String[] args) throws Exception {
        Job job = Job.getInstance(this.getConf(), "phonecount");
        job.setJarByClass(secondOrder.class);
        //todo：1、input
        TextInputFormat.setInputPaths(job, new Path("file:///G:\\bianchengxuexi\\bingdata\\MapReduce_2\\datas\\test1.txt"));
        //todo：2、map
        job.setMapperClass(WCMap.class);
        job.setMapOutputKeyClass(countmain.class);
        job.setMapOutputValueClass(Text.class);
        //todo:3、reduce
        job.setReducerClass(WCReduce.class);
        job.setOutputKeyClass(countmain.class);
        job.setOutputValueClass(Text.class);


        //todo：4、output
        Path path = new Path("file:///G:\\bianchengxuexi\\bingdata\\MapReduce_2\\datas\\up1");
        FileSystem hdfs = FileSystem.get(this.getConf());
        if (hdfs.exists(path)) {
            hdfs.delete(path, true);
        }
        TextOutputFormat.setOutputPath(job, path);
        return job.waitForCompletion(true) ? 0 : -1;
    }

    //定义main
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        //conf.set("fs.defaultFS", "hdfs://192.168.88.161:8020");
        int run = ToolRunner.run(conf, new secondOrder(), args);
        System.exit(run);
    }

    //maper
    public static class WCMap extends Mapper<LongWritable, Text, countmain, Text> {
        countmain countmain = new countmain();
        Text output = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] outkey = value.toString().split(" ");
            output.set(outkey[0]);
            countmain.setA(outkey[0]);
            countmain.setB(Integer.parseInt(outkey[1]));
            context.write(countmain, output);

        }
    }

    //reduce
    private static class WCReduce extends Reducer<countmain, Text, countmain, Text> {

        countmain outputvalue = new countmain();
        Text t = new Text(" ");

        @Override
        protected void reduce(countmain key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            context.write(key, t);
        }
    }

    //定义countmain类
    public static class countmain implements WritableComparable<countmain> {
        private String a;
        private int b;

        public countmain() {
        }

        public void setAll(String a, int b) {
            this.setA(a);
            this.setB(b);
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }

        @Override
        public String toString() {
            return this.getA() + "   " + this.getB();
        }

        public int compareTo(countmain c) {
            int str = this.getA().compareTo(c.getA());
            if (0 == str) {
                return Integer.valueOf(this.getB()).compareTo(Integer.valueOf(c.getB()));

            }
            return str;
        }

        public void write(DataOutput dataOutput) throws IOException {
            dataOutput.writeUTF(this.a);
            dataOutput.writeInt(this.b);

        }

        public void readFields(DataInput dataInput) throws IOException {
            this.a = dataInput.readUTF();
            this.b = dataInput.readInt();
        }


    }

}
