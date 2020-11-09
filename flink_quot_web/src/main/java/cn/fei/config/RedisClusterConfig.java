package cn.fei.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @Date 2020/11/9
 * 获取redis连接对象
 */
@Configuration //表示配置文件，包含bean对象，项目启动，会优先加载配置文件，会把bean置于spring容器进行管理
public class RedisClusterConfig {

    @Value("${redis.maxtotal}")
    private int maxtotal;
    @Value("${redis.minIdle}")
    private int minIdle;
    @Value("${redis.maxIdle}")
    private int maxIdle;
    @Value("${redis.address}")
    private String address;

    @Bean
    public JedisCluster getJedis(){

        //设置连接池
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxTotal(maxtotal);

        //设置
        //node01:7001,node01:7002,node01:7003
        String[] split = address.split(",");
        Set<HostAndPort> set = new HashSet<>();
        for (String str : split) {
            String[] arr = str.split(":");
            set.add(new HostAndPort(arr[0],Integer.valueOf(arr[1])));
        }

        JedisCluster jedisCluster = new JedisCluster(set,jedisPoolConfig);
        return jedisCluster;
    }

}
