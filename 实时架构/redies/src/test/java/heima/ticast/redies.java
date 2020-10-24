package heima.ticast;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Author: 飞
 * Date: 2020/9/15 0015 17:03
 * FileName: redies
 * Description: redies的jdk
 */
public class redies {
    //实例化Jedis
    Jedis jedis = new Jedis();

    //创建jedis客户端
    @Before
    public void jedisClient() {
        //获取连接池配置和连接池对象
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "node1", 6379);
        jedis = jedisPool.getResource();
    }

    //set/get/incr/decr/append/expire/exists/setex/ttl
    @Test
    public void stringtesk() {
        //set/get
        jedis.set("zhangsan", "18");
        jedis.incr("zhangsan");
        jedis.decrBy("zhangsan", 8);
        String s1 = jedis.get("zhangsan");
        System.out.println(s1);
        //结果11
        //append
        jedis.append("zhangsan", "4");
        System.out.println(jedis.get("zhangsan"));
        //expire
//        jedis.expire("zhangsan", 2);
//        while (true) {
//            Long zhangsan = jedis.ttl("zhangsan");
//            System.out.println(zhangsan);
//            if (zhangsan == 0) {
//                break;
//            }
//        }
        //exists
        Boolean s2 = jedis.exists("zhangsan");
        System.out.println("s2=" + s2);
        //setex
        jedis.setex("s2", 10, "55");


    }

    //关闭客户端
    @After
    public void close() {
        jedis.close();
    }
}
