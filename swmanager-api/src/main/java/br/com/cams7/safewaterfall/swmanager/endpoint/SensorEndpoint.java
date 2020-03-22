/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.endpoint;

import static br.com.cams7.safewaterfall.swmanager.endpoint.SensorEndpoint.SENSOR_PATH;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import br.com.cams7.safewaterfall.common.model.vo.SensorVO;
import br.com.cams7.safewaterfall.swmanager.service.SensorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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

  @ApiOperation("Atualiza a ultima leitura do sensor")
  @PostMapping(path = "atualizar")
  @ResponseStatus(value = OK)
  public void atualizarSensor(@ApiParam("Sensor") @RequestBody SensorVO sensor) {
    service.atualizarSensor(sensor);
  }

}
