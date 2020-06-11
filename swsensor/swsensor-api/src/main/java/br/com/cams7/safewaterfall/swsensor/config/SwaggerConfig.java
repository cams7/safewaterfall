/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.config;

import static springfox.documentation.builders.PathSelectors.any;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.google.common.base.Predicate;
import br.com.cams7.safewaterfall.swsensor.endpoint.SensorEndpoint;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author CAMs7
 *
 */
@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
  private static final Predicate<RequestHandler> ENDPOINT_PACKAGE;

  static {
    ENDPOINT_PACKAGE = basePackage(SensorEndpoint.class.getPackage().getName());
  }

  @Bean
  public Docket apiApp() {
    return new Docket(SWAGGER_2).apiInfo(metaData()).select().apis(ENDPOINT_PACKAGE).paths(any()).build();
  }

  private static ApiInfo metaData() {
    return new ApiInfoBuilder().title("Safe Waterfall Sensor").description("Safe Waterfall Sensor").version(
        "0.1.0-SNAPSHOT").contact(new Contact("César A. Magalhães", "https://www.linkedin.com/in/cams7",
            "ceanma@gmail.com")).license("Apache License Version 2.0").licenseUrl(
                "https://www.apache.org/licenses/LICENSE-2.0").build();
  }
}
