package cn.itcast;

import lombok.Data;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.io.CsvReader;
import org.apache.flink.api.java.operators.DataSource;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/20 0020 21:03
 */
public class MixDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<Score> source = env.readCsvFile("G:\\bianchengxuexi\\flink_study\\data\\input\\score.csv")
                .lineDelimiter("\n")
                .fieldDelimiter(",")
                .pojoType(Score.class, "id", "name", "subId", "score");

        source.filter(new FilterFunction<Score>() {
            @Override
            public boolean filter(Score score) throws Exception {
                String name = score.getName();
                return name.equals("王五");

            }
        })
                .reduce(new ReduceFunction<Score>() {
                    @Override
                    public Score reduce(Score score, Score t1) throws Exception {
                        Double score1 = score.score;
                        Double scc = 0D;
                        scc = scc + score1;
                        score.score = scc;
                        return score;
                    }
                })
                .print();
    }

    @Data
    public static class Score {
        private Integer id;
        private String name;
        private Integer subId;
        private Double score;
    }
}
