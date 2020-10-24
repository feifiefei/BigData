package com.itheima.secondhouse;

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
 * Date: 2020/8/22 0022 23:08
 * FileName: secondorice
 * Description: 统计不同地区的平均房价
 */
//定义Drink类
public class secondorice extends Configured implements Tool {
    public int run(String[] args) throws Exception {
        Job job = Job.getInstance(this.getConf(), "lianjiacount1");
        job.setJarByClass(secondorice.class);
        //input
        TextInputFormat.setInputPaths(job, new Path(args[0]));
        //todo：2、map
        job.setMapperClass(WC3Map.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        //shuffle
        job.setPartitionerClass(UserPartition.class);


        //todo:3、reduce
        job.setReducerClass(WC3Reduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setNumReduceTasks(2);//设置reduce个数为2,对应两个分区：part0，part1


        //output
        Path path = new Path(args[1]);
        //如果输出路径存在，删掉重新建立
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
        conf.set("fs.defaultFS", "hdfs://192.168.88.161:8020");
        int run = ToolRunner.run(conf, new secondorice(), args);
        System.exit(run);
    }

    //重写map
    public static class WC3Map extends Mapper<LongWritable, Text, Text, IntWritable> {
        Text outputkey = new Text();
        IntWritable outputValues = new IntWritable();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String str = value.toString().split(",")[3];
            int num1 = Integer.parseInt(value.toString().split(",")[6]);
            this.outputkey.set(str);
            this.outputValues.set(num1);
            context.write(this.outputkey, this.outputValues);


        }
    }

    //重写reduce
    private static class WC3Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

        IntWritable outputvalue = new IntWritable();

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            int sum1 = 0;
            for (IntWritable value : values) {
                sum1 = sum1 + value.get();
                sum = sum + 1;
            }
            this.outputvalue.set(sum1 / sum * 10000);
            context.write(key, this.outputvalue);
        }
    }
}