package HourlyUA;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
        TextInputFormat.setInputPaths(job, new Path("datas\\SogouQ.reduced"));
        //todo：2、map
        job.setMapperClass(WC3Map.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        //shuffle
        job.setGroupingComparatorClass(Grouping.class);
        //todo:3、reduce
        job.setReducerClass(WC3Reduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);


        //output
        TextOutputFormat.setOutputPath(job, new Path("datas\\out2"));
        return job.waitForCompletion(true) ? 0 : -1;
    }

    //定义main
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        //conf.set("fs.defaultFS", "hdfs://192.168.88.161:8020");
        int run = ToolRunner.run(conf, new sougoucount(), args);
        System.exit(run);
    }

    //重写map
    public static class WC3Map extends Mapper<LongWritable, Text, Text, Text> {
        Text outputkey = new Text();
        Text outputValues = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] str = value.toString().split("\t");

            this.outputkey.set(str[0]);
            this.outputValues.set(str[1]);
            context.write(this.outputkey, this.outputValues);


        }
    }

    //重写reduce
    private static class WC3Reduce extends Reducer<Text, Text, Text, Text> {

        Text outputvalue = new Text();
        Set hashSet = new HashSet();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {


            for (Text value : values) {
                hashSet.add(value);
            }
            this.outputvalue.set(String.valueOf(hashSet.size()));
            hashSet.clear();
            context.write(key, this.outputvalue);
        }
    }

    private static class Grouping extends WritableComparator {
        //注册
        public Grouping() {
            super(Text.class, true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            Text c = (Text) a;
            Text d = (Text) b;
            return c.toString().substring(0, 3).compareTo(d.toString().substring(0, 3));
        }

    }

}
