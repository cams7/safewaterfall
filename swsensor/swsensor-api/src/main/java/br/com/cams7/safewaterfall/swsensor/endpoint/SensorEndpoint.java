/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.endpoint;

import static br.com.cams7.safewaterfall.swsensor.endpoint.SensorEndpoint.SENSOR_PATH;
import static br.com.cams7.safewaterfall.swsensor.model.SensorEntity.getSensor;
import static br.com.cams7.safewaterfall.swsensor.scheduler.AppQuartzConfig.STATUS_ARDUINO_TRIGGER;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import br.com.cams7.safewaterfall.common.model.vo.AppSensorVO;
import br.com.cams7.safewaterfall.common.service.AppSchedulerService;
import br.com.cams7.safewaterfall.common.service.AppSensorService;
import br.com.cams7.safewaterfall.swsensor.model.SensorEntity;
import br.com.cams7.safewaterfall.swsensor.service.SensorService;
import br.com.cams7.safewaterfall.swsensor.service.StatusArduinoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author CAMs7
 *
 */
@Api("Endpoint utilizado para executar as funcionalidades do sensor.")
@RestController
@RequestMapping(path = SENSOR_PATH, produces = APPLICATION_JSON_UTF8_VALUE)
public class SensorEndpoint {

  public static final String SENSOR_PATH = "/sensor";

  @Autowired
  private StatusArduinoService arduinoService;

  @Autowired
  private SensorService sensorService;

  @Autowired
  private AppSensorService appSensorService;

  @Autowired
  private AppSchedulerService appSchedulerService;

  @ApiOperation("Carrega a distancia medida (em milimetros) pelo sensor na memoria")
  @GetMapping(path = "/load_distance")
  @ResponseStatus(value = OK)
  public void loadDistance() {
    arduinoService.loadDistance();
  }

  @ApiOperation("Busca a distancia (em milimetros) medida pelo sensor que foi carregada previamente")
  @GetMapping(path = "/distance")
  @ResponseStatus(value = OK)
  public short getDistance() {
    short distance = arduinoService.getDistance();
    return distance;
  }

  @ApiOperation("Salva ou atualiza os dados do sensor")
  @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(value = OK)
  public void save(@ApiParam("Sensor") @Valid @RequestBody AppSensorVO appSensor) {
    SensorEntity sensor = sensorService.save(getSensor(appSensor));
    appSchedulerService.reschedule(STATUS_ARDUINO_TRIGGER, sensor.getStatusArduinoCron());
    appSensorService.save(appSensor);
  }

  @ApiOperation("Buscar o sensor pelo ID")
  @GetMapping(path = "{id}")
  @ResponseStatus(value = OK)
  public AppSensorVO findById(@ApiParam("ID do sensor") @PathVariable Long id) {
    SensorEntity sensor = sensorService.findById(id);
    AppSensorVO appSensor = getSensor(sensor);
    return appSensor;
  }

}
