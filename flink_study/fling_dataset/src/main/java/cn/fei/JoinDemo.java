package cn.itcast;


import lombok.Data;
import org.apache.flink.api.common.functions.FlatJoinFunction;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.util.Collector;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/21 0021 16:47
 */
public class JoinDemo {
    public static void main(String[] args) throws Exception {
        //获取环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //数据加载
        DataSource<Score> score = env.readCsvFile("G:\\bianchengxuexi\\flink_study\\data\\input\\score.csv")
                .pojoType(Score.class, "id", "name", "subId", "score");
        DataSource<Subject> subject = env.readCsvFile("G:\\bianchengxuexi\\flink_study\\data\\input\\subject.csv")
                .pojoType(Subject.class, "subId", "subName");
        //数据处理
        score
                .join(subject)
                .where("subId")
                .equalTo("subId")
                .with(new JoinFunction<Score, Subject, Object>() {
                    @Override
                    public Object join(Score score, Subject subject) throws Exception {
                        return Tuple4.of(score.getId(), score.getName(), subject.getSubName(), score.getScore());
                    }
                })
                .print();

    }

    @Data
    public static class Subject {
        private Integer subId;
        private String subName;
    }

    @Data
    public static class Score {
        private Integer id;
        private String name;
        private Integer subId;
        private Double score;
    }
}
