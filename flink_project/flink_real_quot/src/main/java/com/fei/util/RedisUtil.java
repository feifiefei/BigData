package com.fei.util;

import com.fei.config.QuotConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;

/**
 * @Date 2020/10/31
 * 集群连接对象
 */
public class RedisUtil {

    /**
     * 1.新建获取连接的方法
     * 2.初始化连接池
     * 3.设置连接集群地址
     * 4.获取客户端连接对象
     */
    public static JedisCluster getJedisCluster(){
        /**
         * redis.host=node01:7001,node01:7002,node01:7003
         * redis.maxTotal=10
         * redis.minIdle=2
         * redis.maxIdle=5
         */
        //获取配置文件redis连接池参数
        String maxTotal = QuotConfig.config.getProperty("redis.maxTotal");
        String minIdle = QuotConfig.config.getProperty("redis.minIdle");
        String maxIdle = QuotConfig.config.getProperty("redis.maxIdle");
        //设置连接池
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(Integer.valueOf(maxTotal)); //最大连接数
        jedisPoolConfig.setMinIdle(Integer.valueOf(minIdle)); //最小空闲连接数
        jedisPoolConfig.setMaxIdle(Integer.valueOf(maxIdle)); //最大空闲连接数

        //设置集群ip和端口
        HashSet<HostAndPort> set = new HashSet<>();
        String host = QuotConfig.config.getProperty("redis.host");
        String[] split = host.split(",");
        for (String singleHost : split) {
            //node01:7001
            String[] arr = singleHost.split(":");
            set.add(new HostAndPort(arr[0],Integer.valueOf(arr[1])));
        }

        JedisCluster jedisCluster = new JedisCluster(set, jedisPoolConfig);
        return jedisCluster;
    }

    public static void main(String[] args) {

        //获取连接对象
        JedisCluster jedisCluster = getJedisCluster();
        String hget = jedisCluster.hget("product", "apple");
        System.out.println(hget);
    }
}
