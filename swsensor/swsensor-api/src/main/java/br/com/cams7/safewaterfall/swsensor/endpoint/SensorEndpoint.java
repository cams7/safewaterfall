/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.endpoint;

import static br.com.cams7.safewaterfall.swsensor.endpoint.SensorEndpoint.SENSOR_PATH;
import static br.com.cams7.safewaterfall.swsensor.scheduler.AppQuartzConfig.STATUS_ARDUINO_TRIGGER;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author CAMs7
 *
 */
@Api("Endpoint utilizado para realiazar o CRUD do Sensor.")
@RestController
@RequestMapping(path = SENSOR_PATH, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
public class SensorEndpoint {

  public static final String SENSOR_PATH = "/sensor";

  @Autowired
  private SensorService sensorService;

  @Autowired
  private AppSensorService appSensorService;

  @Autowired
  private AppSchedulerService appSchedulerService;

  @ApiOperation("Salva ou atualiza os dados do sensor")
  @ResponseStatus(value = CREATED)
  @PostMapping
  public void save(@ApiParam("Sensor") @Valid @RequestBody SensorEntity sensor) {
    sensor = sensorService.save(sensor);

    appSchedulerService.reschedule(STATUS_ARDUINO_TRIGGER, sensor.getStatusArduinoCron());

    AppSensorVO vo = appSensorService.findById(sensor.getId());

    vo.setStatusArduinoCron(sensor.getStatusArduinoCron());
    vo.setSendStatusMessageCron(sensor.getSendStatusMessageCron());
    vo.setSendAlertMessageCron(sensor.getSendAlertMessageCron());
    vo.setMinimumAllowedDistance(sensor.getMinimumAllowedDistance());
    vo.setMaximumMeasuredDistance(sensor.getMaximumMeasuredDistance());

    appSensorService.save(vo);
  }

  @ApiOperation("Buscar o sensor pelo ID")
  @ResponseStatus(value = OK)
  @GetMapping(path = "{id}", consumes = {ALL_VALUE})
  public SensorEntity findById(@ApiParam("ID do sensor") @PathVariable Long id) {
    SensorEntity sensor = sensorService.findById(id);
    return sensor;
  }

}
