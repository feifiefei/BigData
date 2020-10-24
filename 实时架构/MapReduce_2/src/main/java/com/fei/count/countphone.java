package com.itheima.count;

import com.google.inject.internal.cglib.core.$LocalVariablesSorter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * Author: 飞
 * Date: 2020/8/24 0024 17:39
 * FileName: countphone
 * Description: 统计流量
 */
public class countphone extends Configured implements Tool {

    public int run(String[] args) throws Exception {
        Job job = Job.getInstance(this.getConf(), "phonecount");
        job.setJarByClass(countphone.class);
        //todo：1、input
        TextInputFormat.setInputPaths(job, new Path("file:///G:\\bianchengxuexi\\bingdata\\MapReduce_2\\datas\\data_flow.dat"));
        //todo：2、map
        job.setMapperClass(WCMap.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(countmain.class);
        //todo:3、reduce
        job.setReducerClass(WCReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(countmain.class);


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
        int run = ToolRunner.run(conf, new countphone(), args);
        System.exit(run);
    }

    //maper
    public static class WCMap extends Mapper<LongWritable, Text, Text, countmain> {
        Text outputkey = new Text();
        countmain countmain = new countmain();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] outkey = value.toString().split("\t");
            if (outkey.length == 11) {
                outputkey.set(outkey[1]);
                countmain.setAll(Integer.parseInt(outkey[6]), Integer.parseInt(outkey[7]), Integer.parseInt(outkey[8]), Integer.parseInt(outkey[9]));
                context.write(outputkey, countmain);
            } else {
                return;
            }
        }
    }

    //reduce
    private static class WCReduce extends Reducer<Text, countmain, Text, countmain> {

        countmain outputvalue = new countmain();

        @Override
        protected void reduce(Text key, Iterable<countmain> values, Context context) throws IOException, InterruptedException {
            int sum1 = 0;
            int sum2 = 0;
            int sum3 = 0;
            int sum4 = 0;
            for (countmain value : values) {
                sum1 += value.getUpPack();
                sum2 += value.getDownPack();
                sum3 += value.getUpPayload();
                sum4 += value.getDownPayload();

            }
            this.outputvalue.setAll(sum1, sum2, sum3, sum4);
            context.write(key, this.outputvalue);
        }
    }
}
