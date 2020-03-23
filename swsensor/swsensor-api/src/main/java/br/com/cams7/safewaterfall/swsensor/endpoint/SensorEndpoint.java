/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.endpoint;

import static br.com.cams7.safewaterfall.swsensor.endpoint.SensorEndpoint.SENSOR_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.cams7.safewaterfall.swsensor.service.SensorService;
import io.swagger.annotations.Api;

/**
 * @author CAMs7
 *
 */
@Api("Endpoint utilizado para executar as funcionalidades do Sensor.")
@RestController
@RequestMapping(path = SENSOR_PATH, produces = APPLICATION_JSON_UTF8_VALUE)
public class SensorEndpoint {

  public static final String SENSOR_PATH = "/sensor";

  @Autowired
  private SensorService service;

}
