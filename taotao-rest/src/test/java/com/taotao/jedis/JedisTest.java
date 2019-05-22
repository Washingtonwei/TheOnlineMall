package com.taotao.jedis;

import java.util.HashSet;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {
	@Test
	public void testJedisSingle() {
		// Create a Jedis object
		Jedis jedis = new Jedis("10.0.60.207", 6379);
		// invoke Jedis object's methods, method names are consistent with redis
		// command line
		jedis.set("key1", "jedis test");
		System.out.println(jedis.get("key1"));
		// close jedis
		jedis.close();
	}

	/**
	 * It is wasteful to create a jedis object eveytime, so use jedis pool
	 */
	@Test
	public void testJedisPool() {
		// create jedis pool
		JedisPool pool = new JedisPool("10.0.60.207", 6379);
		// get jedis object fromt his pool
		Jedis jedis = pool.getResource();
		String string = jedis.get("key1");
		System.out.println(string);
		// close jedis
		jedis.close();// pool will reclaim this object, think of like put the
						// object back to the pool
		// close jedis pool
		pool.close();
	}

	@Test
	/**
	 * Don't forget to open firewall
	 */
	public void testJedisCluster() {
		HashSet<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("10.0.60.207", 7001));
		nodes.add(new HostAndPort("10.0.60.207", 7002));
		nodes.add(new HostAndPort("10.0.60.207", 7003));
		nodes.add(new HostAndPort("10.0.60.207", 7004));
		nodes.add(new HostAndPort("10.0.60.207", 7005));
		nodes.add(new HostAndPort("10.0.60.207", 7006));

		// pass the above set to cluster object
		JedisCluster cluster = new JedisCluster(nodes);

		cluster.set("key1", "1000");
		String string = cluster.get("key1");
		System.out.println(string);

		cluster.close();
	}

	@Test
	public void testSpringJedisSingle() {
		// create the container
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-*.xml");
		JedisPool pool = (JedisPool) applicationContext.getBean("redisClient");
		Jedis jedis = pool.getResource();
		String string = jedis.get("key1");
		System.out.println(string);
		// close jedis
		jedis.close();// pool will reclaim this object, think of like put the
						// object back to the pool
		// close jedis pool
		pool.close();
	}

	@Test
	public void testSpringJedisCluster() {
		// create the container
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-*.xml");
		JedisCluster cluster = (JedisCluster) applicationContext.getBean("redisClient");
		cluster.set("key1", "1000");
		String string = cluster.get("key1");
		System.out.println(string);

		cluster.close();
	}

}
