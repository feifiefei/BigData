package cn.fei;

import cn.fei.mapper.QuotMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.Map;

/**
 * @Date 2020/11/7
 */
// 获取启动类，加载配置，确定装载 Spring 程序的装载方法
@SpringBootTest
// 让 JUnit 运行 Spring 的测试环境，获得 Spring 环境的上下文的支持
@RunWith(SpringRunner.class)
public class TestQuery {

    @Autowired
    QuotMapper quotMapper;



    @Test
    public void mybatisQuery(){

        List<Map<String, Object>> query = quotMapper.query();
        System.out.println("<<<<:"+query);
    }

    @Autowired
    JedisCluster jedisCluster;

    @Test
    public void redisTest(){

        String str = jedisCluster.hget("quot", "turnoverRate");
        System.out.println("<<<<:"+str);
    }


}
