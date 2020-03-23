package br.com.cams7.safewaterfall;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@SpringBootApplication
@EnableScheduling
public class App {

  @Bean
  public RestOperations restTemplate() {
    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
    // Connect timeout
    clientHttpRequestFactory.setConnectTimeout(5_000);// 5 secounds
    // Read timeout
    clientHttpRequestFactory.setReadTimeout(5_000);// 5 secounds
    return new RestTemplate(clientHttpRequestFactory);
  }

  @Bean
  public Module module() {
    return new Hibernate5Module();
  }

  @SuppressWarnings("deprecation")
  @Bean
  public JedisConnectionFactory jedisConnectionFactory() {
    JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
    jedisConFactory.setHostName("172.42.42.210");
    jedisConFactory.setPort(6479);
    return jedisConFactory;
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(jedisConnectionFactory());
    return template;
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder(App.class).bannerMode(Mode.OFF).run(args);
  }

}
