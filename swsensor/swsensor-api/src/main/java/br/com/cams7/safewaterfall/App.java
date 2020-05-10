package br.com.cams7.safewaterfall;

import static org.springframework.boot.Banner.Mode.OFF;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${HTTP_TIMEOUT}")
  private String httpTimeout;

  @Value("${REDIS_ADDRESS}")
  private String redisAddress;

  @Bean
  public RestOperations restTemplate() {
    final int TIMEOUT = Integer.parseInt(httpTimeout);
    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
    // Connect timeout
    clientHttpRequestFactory.setConnectTimeout(TIMEOUT);
    // Read timeout
    clientHttpRequestFactory.setReadTimeout(TIMEOUT);
    return new RestTemplate(clientHttpRequestFactory);
  }

  @Bean
  public Module module() {
    return new Hibernate5Module();
  }

  @SuppressWarnings("deprecation")
  @Bean
  public JedisConnectionFactory jedisConnectionFactory() {
    String[] address = redisAddress.split(":");
    final String REDIS_HOSTNAME = address[0];
    final int REDIS_PORT = Integer.parseInt(address[1]);
    JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
    jedisConFactory.setHostName(REDIS_HOSTNAME);
    jedisConFactory.setPort(REDIS_PORT);
    return jedisConFactory;
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(jedisConnectionFactory());
    return template;
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder(App.class).bannerMode(OFF).run(args);
  }

}
