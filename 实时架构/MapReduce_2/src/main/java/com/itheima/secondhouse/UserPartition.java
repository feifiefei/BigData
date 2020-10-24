package com.itheima.secondhouse;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * Author: 飞
 * Date: 2020/8/24 0024 12:22
 * FileName: UserPartition
 * Description:重写Partitioner
 */
public class UserPartition extends Partitioner<Text, IntWritable> {
    @Override
    public int getPartition(Text text, IntWritable intWritable, int i) {
        //获取当前数据的地区
        String region = text.toString();
        if ("浦东".equals(region)) {
            return 0;
        } else {
            return 1;
        }
    }
}
