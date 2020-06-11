package br.com.cams7.safewaterfall;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCaching
public class App {

  @Value("${HTTP_TIMEOUT}")
  private String httpTimeout;

  @Bean
  public RestTemplate restTemplate() {
    final int TIMEOUT = Integer.parseInt(httpTimeout);
    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
    // Connect timeout
    clientHttpRequestFactory.setConnectTimeout(TIMEOUT);
    // Read timeout
    clientHttpRequestFactory.setReadTimeout(TIMEOUT);
    return new RestTemplate(clientHttpRequestFactory);
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

}
