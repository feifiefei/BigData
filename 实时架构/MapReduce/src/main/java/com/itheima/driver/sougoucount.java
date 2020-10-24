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
 * Date: 2020/8/22 0022 23:23
 * FileName: sougoucount
 * Description: 统计搜狗热词
 */
public class sougoucount extends Configured implements Tool {
    public int run(String[] args) throws Exception {
        Job job = Job.getInstance(this.getConf(), "lianjiacount11");
        job.setJarByClass(sougoucount.class);
        //input
        TextInputFormat.setInputPaths(job, new Path(args[0]));
        //todo：2、map
        job.setMapperClass(WC3Map.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        //todo:3、reduce
        job.setReducerClass(WC3Reduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);


        //output
        TextOutputFormat.setOutputPath(job, new Path(args[1]));
        return job.waitForCompletion(true) ? 0 : -1;
    }

    //定义main
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://192.168.88.161:8020");
        int run = ToolRunner.run(conf, new sougoucount(), args);
        System.exit(run);
    }

    //重写map
    public static class WC3Map extends Mapper<LongWritable, Text, Text, IntWritable> {
        Text outputkey = new Text();
        IntWritable outputValues = new IntWritable(1);

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String str = value.toString().split("\t")[2];
            this.outputkey.set(str);
            context.write(this.outputkey, this.outputValues);


        }
    }

    //重写reduce
    private static class WC3Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

        IntWritable outputvalue = new IntWritable();

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

            int sum1 = 0;
            for (IntWritable value : values) {
                sum1 = sum1 + value.get();
            }
            this.outputvalue.set(sum1);
            context.write(key, this.outputvalue);
        }
    }

}
