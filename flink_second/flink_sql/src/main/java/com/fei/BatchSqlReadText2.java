package com.fei;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.BatchTableEnvironment;
import org.apache.flink.table.sources.CsvTableSource;
import org.apache.flink.types.Row;

/**
 * @description:todo: flink的批处理SQL之通过CsvTableSource构建数据源
 * @author: 飞
 * @date: 2020/10/25 0025 20:35
 */
public class BatchSqlReadText2 {
    public static void main(String[] args) throws Exception {
        /**
         * 需求：求各班级每个学科的平均分、三科总分平均分
         * （1）通过CsvTableSource构建数据源
         * （2）注册表tblEnv.registerTableSource("score",score);
         */
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //构建表的执行环境
        BatchTableEnvironment tblEnv = BatchTableEnvironment.create(env);
        //构建数据源
        CsvTableSource scores = CsvTableSource.builder()
                .path("G:\\bianchengxuexi\\GitWarehouse\\BigData\\flink_second\\datas\\sql_scores.txt")
                .field("id", Types.INT)
                .field("chinese", Types.INT)
                .field("math", Types.INT)
                .field("english", Types.INT)
                .build();
        //构建student
        CsvTableSource students = CsvTableSource.builder()
                .path("G:\\bianchengxuexi\\GitWarehouse\\BigData\\flink_second\\datas\\sql_students.txt")
                .field("id", Types.INT)
                .field("name", Types.STRING)
                .field("classname", Types.STRING)
                .build();
        //注册表
        tblEnv.registerTableSource("score", scores);
        tblEnv.registerTableSource("student", students);
        //写SQL语句
        String sql = "select a.classname,avg(b.chinese),avg(b.math),avg(b.english),avg(b.chinese+b.math+b.english) from student a inner join score b on a.id = b.id group by a.classname";
        //SQL查询
        Table table = tblEnv.sqlQuery(sql);
        //table转换为dataset
        //row类似于object，可用于接收多个字段
        DataSet<Row> rowDataSet = tblEnv.toDataSet(table, Row.class);
        rowDataSet.print();

    }
}
