package takeTheTopThree;

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

import java.io.IOException;
import java.util.*;

/**
 * Author: 飞
 * Date: 2020/8/22 0022 23:23
 * FileName: sougoucount
 * Description: 统计搜狗热词
 */
public class sougouTop3 extends Configured implements Tool {
    public int run(String[] args) throws Exception {
        Job job = Job.getInstance(this.getConf(), "sougou11");
        job.setJarByClass(sougouTop3.class);
        //input
        TextInputFormat.setInputPaths(job, new Path("datas\\SogouQ.reduced"));
        //todo：2、map
        job.setMapperClass(WC3Map.class);
        job.setMapOutputKeyClass(SgouBean.class);
        job.setMapOutputValueClass(NullWritable.class);
        //shuffle
        job.setGroupingComparatorClass(Grouping.class);
        //todo:3、reduce
        job.setReducerClass(WC3Reduce.class);
        job.setOutputKeyClass(SgouBean.class);
        job.setOutputValueClass(NullWritable.class);


        //output
        Path path = new Path("datas\\out3");
        FileSystem hdfs = FileSystem.newInstance(getConf());
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
        int run = ToolRunner.run(conf, new sougouTop3(), args);
        System.exit(run);
    }

    //重写map
    public static class WC3Map extends Mapper<LongWritable, Text, SgouBean, NullWritable> {
        SgouBean outputkey = new SgouBean();

        NullWritable outputValues = NullWritable.get();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] str = value.toString().split("\t");

            this.outputkey.setAll(str[0], str[2]);
            context.write(this.outputkey, this.outputValues);


        }
    }

    //重写reduce
    private static class WC3Reduce extends Reducer<Text, Text, SgouBean, NullWritable> {
        List<Text> list = new ArrayList();
        SgouBean outputvalue = new SgouBean();
        NullWritable outputkey = NullWritable.get();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

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
