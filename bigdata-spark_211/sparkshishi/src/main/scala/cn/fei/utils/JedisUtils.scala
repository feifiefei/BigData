package cn.fei.utils

import java.util.Properties

import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

/**
 * 编写 JedisPool工具类
 */
object JedisUtils {
	
	/** 返回JedisPool连接池对象实例 */
	def getJedisPoolInstance(host: String, port: Int, props: Properties = new Properties()): JedisPool = {
		// 创建池子的配置对象，将配置信息放到对象中
		val poolConfig: JedisPoolConfig = new JedisPoolConfig
		// 设置 最大连接数
		poolConfig.setMaxTotal(props.getProperty("redis.max.total", "10").toInt)
		// 设置最大的空闲数
		poolConfig.setMaxIdle(props.getProperty("redis.max.idle", "5").toInt)
		// 设置最小的空闲数
		poolConfig.setMinIdle(props.getProperty("redis.min.idle", "2").toInt)
		// 创建一个JedisPool连接池
		new JedisPool(poolConfig, host, port)
	}
	
	/** 释放Jedis连接对象到连接池中 */
	def release(jedis: Jedis): Unit = {
		// 将Jedis连接对象放回到池中
		jedis.close()
	}
}
