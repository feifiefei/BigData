package com.itheima.driver;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
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
 * Date: 2020/8/22 0022 18:09
 * FileName: MRMode
 * Description: 一个MapReduce的标准模板
 */
public class MRMode extends Configured implements Tool {
    //todo:1、重写run方法
    public int run(String[] args) throws Exception {
        Job job = Job.getInstance(this.getConf(), "MRmode");
        job.setJarByClass(MRMode.class);
        //todo:2、input
        Path inputpath = new Path(args[0]);
        TextInputFormat.setInputPaths(job, inputpath);
        //todo：2、map
        job.setMapperClass(MRModeMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
//todo:3、reduce
        job.setReducerClass(MRModeReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
//todo:4、output
        TextOutputFormat.setOutputPath(job, new Path(args[1]));
        return job.waitForCompletion(true) ? 0 : -1;
    }

    //程序入口
    public static void main(String[] args) throws Exception {
        ToolRunner.run(new Configuration(), new MRMode(), args);
    }

    //定义Map类
    public static class MRModeMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //自定义
        }
    }

    //定义Reduce
    public static class MRModeReduce extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            //自定义
        }
    }

}
