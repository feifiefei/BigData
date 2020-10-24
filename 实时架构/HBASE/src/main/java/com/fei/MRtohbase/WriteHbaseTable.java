package com.itheima.MRtohbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * @ClassName WriteHbaseTable
 * @Description TODO 通过MapReduce将数据写入Hbase
 * @Create By     Frank
 */
public class WriteHbaseTable extends Configured implements Tool {

    public int run(String[] args) throws Exception {
        //todo:1-创建
        Job job =  Job.getInstance(this.getConf(),"write");
        job.setJarByClass(WriteHbaseTable.class);
        //todo:2-配置
        //input
        TextInputFormat.setInputPaths(job,new Path(args[0]));
        //map
        job.setMapperClass(WriteToHbaseMap.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Put.class);
        //shuffle
        //reduce&output
        /**
         *  public static void initTableReducerJob(
         *     String table,                                将数据写入Hbase的哪张表
         *     Class<? extends TableReducer> reducer,       reducer的类
         *     Job job)                                     当前的job
         *
         *     以前输出的写法：
         *      job.setoutputKey：因为Key可以任意的，这里根本用不到
         *      job.setoutputValue：在TableReduce中将outputValue定死了，所以不用写
         *
         */
        TableMapReduceUtil.initTableReducerJob(
            "student:mrwrite",
            WriteToHbaseReduce.class,
            job
        );
        //todo:3-提交
        return job.waitForCompletion(true) ? 0:-1;
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        int status = ToolRunner.run(conf, new WriteHbaseTable(), args);
        System.exit(status);
    }

    /**
     * 读取文件，将文件中的内容，id作为key，其他的每一列作为一个Put对象
     */
    public static class WriteToHbaseMap extends Mapper<LongWritable,Text,Text, Put>{

        Text rowkey = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //value：1	liudehua	18	male
            String[] split = value.toString().split("\t");
            String row = split[0];
            String name = split[1];
            String age = split[2];
            String sex = split[3];
            //将id作为rowkey，放在key中输出
            this.rowkey.set(split[0]);
            //构造输出的Value
            Put putname = new Put(Bytes.toBytes(row));
            putname.addColumn(Bytes.toBytes("info"),Bytes.toBytes("name"),Bytes.toBytes(name));
            context.write(rowkey,putname);
            Put putage = new Put(Bytes.toBytes(row));
            putage.addColumn(Bytes.toBytes("info"),Bytes.toBytes("age"),Bytes.toBytes(age));
            context.write(rowkey,putage);
            Put putsex = new Put(Bytes.toBytes(row));
            putsex.addColumn(Bytes.toBytes("info"),Bytes.toBytes("sex"),Bytes.toBytes(sex));
            context.write(rowkey,putsex);
        }
    }

    /**
     * public abstract class TableReducer<KEYIN, VALUEIN, KEYOUT>
     * extends Reducer<KEYIN, VALUEIN, KEYOUT, Mutation>
     *     最后Reduce输出的Value类型必须为Put类型，才能将数据写入Hbase
     */
    public static class WriteToHbaseReduce extends TableReducer<Text,Put,Text>{
        /**
         * 相同rowkey的所有Put都在一个迭代器中
         * @param key
         * @param values
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void reduce(Text key, Iterable<Put> values, Context context) throws IOException, InterruptedException {
            //直接遍历每个put对象，输出即可
            for (Put value : values) {
                context.write(key,value);
            }
        }
    }

}
