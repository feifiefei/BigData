package com.itheima.MRtohbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * @ClassName ReadHbaseTable
 * @Description TODO 通过MapReduce读取Hbase表中的数据
 * @Create By     Frank
 */
public class ReadHbaseTable extends Configured implements Tool {

    public int run(String[] args) throws Exception {
        //todo:1-创建
        Job job =  Job.getInstance(this.getConf(),"read");
        job.setJarByClass(ReadHbaseTable.class);
        //todo:2-配置
        //input&map
        /**
         * public static void initTableMapperJob(
         *       String table,                              指定从哪张表读取
         *       Scan scan,                                 读取Hbase数据使用的Scan对象，自定义过滤器
         *       Class<? extends TableMapper> mapper,       Mapper类
         *       Class<?> outputKeyClass,                   Map输出的Key类型
         *       Class<?> outputValueClass,                 Map输出的Value类型
         *       Job job                                    当前的job
         *  )
         */
        //构建TableInputFormat用于读取Hbase的scan对象
        Scan scan = new Scan();//为了方便让你使用过滤器，提前过滤数据，再传递到MapReduce中，所以让你自定义一个scan对象
        //可以为scan设置过滤器
        TableMapReduceUtil.initTableMapperJob(
                "student:stu_info",
                scan,
                ReadHbaseMap.class,
                Text.class,
                Text.class,
                job
        );
        //reduce
        job.setNumReduceTasks(0);
        //output
        TextOutputFormat.setOutputPath(job,new Path("datas/output/hbase"));
        //todo:3-提交
        return job.waitForCompletion(true) ? 0:-1;
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "node1:2181,node2:2181,node3:2181");
        int status = ToolRunner.run(conf, new ReadHbaseTable(), args);
        System.exit(status);
    }

    /**
     * TableMapper<KEYOUT, VALUEOUT>
     * extends Mapper<ImmutableBytesWritable, Result, KEYOUT, VALUEOUT>
     */
    public static class ReadHbaseMap extends TableMapper<Text, Text>{
        //rowkey
        Text outputKey = new Text();
        //每一列的数据
        Text outputValue = new Text();


        /**
         * 每个KV【一个Rowkey】调用一次map方法
         * @param key：rowkey
         * @param value：这个rowkey的数据
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
            //给key进行赋值
            String rowkey = Bytes.toString(key.get());
            this.outputKey.set(rowkey);
            //给value赋值
            for(Cell cell : value.rawCells()){
                //得到每一列的数据
                String family = Bytes.toString(CellUtil.cloneFamily(cell));
                String column = Bytes.toString(CellUtil.cloneQualifier(cell));
                String val  = Bytes.toString(CellUtil.cloneValue(cell));
                long ts = cell.getTimestamp();
                this.outputValue.set(family+"\t"+column+"\t"+val+"\t"+ts);
                context.write(this.outputKey,this.outputValue);
            }
        }
    }
}
