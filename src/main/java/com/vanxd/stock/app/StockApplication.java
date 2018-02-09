package com.vanxd.stock.app;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.reactive.function.client.WebClient;

import javax.sql.DataSource;

@SpringBootApplication
@ComponentScan("com.vanxd.stock")
@EnableCaching
public class StockApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockApplication.class, args);
	}

	@Bean(name = "dataSouce")
	@Primary
	public DataSource oracleDataSouce() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUsername("root");
		dataSource.setPassword("xiaodong");
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://192.168.99.100:3306/stock_analysis?useUnicode=true&characterEncoding=utf-8");
		return dataSource;
	}
	@Bean
	public WebClient getWebClient() {
		return WebClient.create();
	}

	@Bean
	public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(factory);
		//key序列化方式;但是如果方法上有Long等非String类型的话，会报类型转换错误；
		RedisSerializer<String> redisSerializer = new StringRedisSerializer();//Long类型不可以会出现异常信息;
		redisTemplate.setKeySerializer(redisSerializer);
		redisTemplate.setHashKeySerializer(redisSerializer);

		//JdkSerializationRedisSerializer序列化方式;
		JdkSerializationRedisSerializer jdkRedisSerializer=new JdkSerializationRedisSerializer();
		redisTemplate.setValueSerializer(jdkRedisSerializer);
		redisTemplate.setHashValueSerializer(jdkRedisSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
}
