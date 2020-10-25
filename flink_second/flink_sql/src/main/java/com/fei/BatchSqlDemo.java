package com.fei;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.JoinOperator;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.BatchTableEnvironment;
import org.apache.flink.types.Row;

/**
 * @description:todo: flink的批处理sql之bean对象版本
 * @author: 飞
 * @date: 2020/10/25 0025 15:35
 */
public class BatchSqlDemo {
    public static void main(String[] args) throws Exception {
        /**
         * 开发步骤：
         * 	1.初始化批处理执行环境
         * 	2.获取表执行环境
         * 	3.加载数据源
         * 	4.数据转换，新建bean对象
         * 	5.数据关联
         * 	6.注册视图
         * 	7.查询sql
         * 	8.转换成DataSet
         * 	9.数据打印
         */
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //获取表执行环境
        BatchTableEnvironment tblEnv = BatchTableEnvironment.create(env);
        //加载数据源
        DataSource<String> scoreSource = env.readTextFile("G:\\bianchengxuexi\\GitWarehouse\\BigData\\flink_second\\datas\\sql_scores.txt");
        DataSource<String> studentSource = env.readTextFile("G:\\bianchengxuexi\\GitWarehouse\\BigData\\flink_second\\datas\\sql_students.txt");
        MapOperator<String, Score> sorceBean = scoreSource.map(new MapFunction<String, Score>() {
            @Override
            public Score map(String s) throws Exception {
                String[] split = s.split(",");
                return new Score(
                        Integer.valueOf(split[0]),
                        Integer.valueOf(split[1]),
                        Integer.valueOf(split[2]),
                        Integer.valueOf(split[3])
                );
            }
        });
        //转换student对象
        MapOperator<String, Student> studentBean = studentSource.map(new MapFunction<String, Student>() {
            @Override
            public Student map(String s) throws Exception {
                String[] split = s.split(",");
                return new Student(
                        Integer.valueOf(split[0]),
                        split[1],
                        split[2]
                );
            }
        });
        JoinOperator.EquiJoin<Student, Score, StudentScore> studentScoreBean = studentBean.join(sorceBean).where("id").equalTo("id")
                .with(new JoinFunction<Student, Score, StudentScore>() {
                    @Override
                    public StudentScore join(Student student, Score s) throws Exception {
                        return new StudentScore(
                                student.getClassname(),
                                s.getChinese(),
                                s.getMath(),
                                s.getEnglish()
                        );
                    }
                });
        //注册视图/注册表
        tblEnv.createTemporaryView("tbl", studentScoreBean, "classname,chinese,math,english");
        //查询SQL
        String sql = "select classname,avg(chinese),avg(math),avg(english),avg(chinese+math+english) from tbl group by classname";
        Table table = tblEnv.sqlQuery(sql);
        //table转换成DataSet
        DataSet<Row> rowDataSet = tblEnv.toDataSet(table, Row.class);
        //数据写入文件
        rowDataSet
                .writeAsText("bathcsql.txt", FileSystem.WriteMode.OVERWRITE)
                .setParallelism(1);
        //触发执行
        env.execute();


    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    //合并后得数据
    public static class StudentScore {
        private String classname;
        private Integer chinese;
        private Integer math;
        private Integer english;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Score {
        private Integer id;
        private Integer chinese;
        private Integer math;
        private Integer english;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Student {
        private Integer id;
        private String name;
        private String classname;

    }
}
