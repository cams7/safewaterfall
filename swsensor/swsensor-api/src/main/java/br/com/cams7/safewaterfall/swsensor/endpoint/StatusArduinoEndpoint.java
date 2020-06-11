package br.com.cams7.safewaterfall.swsensor.endpoint;

import static br.com.cams7.safewaterfall.AppConstants.PAUSE_PATH;
import static br.com.cams7.safewaterfall.AppConstants.RESCHEDULE_PATH;
import static br.com.cams7.safewaterfall.AppConstants.RESUME_PATH;
import static br.com.cams7.safewaterfall.swsensor.endpoint.StatusArduinoEndpoint.STATUS_ARDUINO_PATH;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import br.com.cams7.safewaterfall.common.model.Sensor;
import br.com.cams7.safewaterfall.swsensor.cron.StatusArduinoCron;
import br.com.cams7.safewaterfall.swsensor.model.vo.CronVO;
import br.com.cams7.safewaterfall.swsensor.service.SensorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Endpoint utilizado para executar as funcionalidades do escalonador do status do arduino.")
@RestController
@RequestMapping(path = STATUS_ARDUINO_PATH)
public class StatusArduinoEndpoint {

  public static final String STATUS_ARDUINO_PATH = "/status_arduino";

  @Value("${SENSOR_ID}")
  private String sensorId;

  @Autowired
  private StatusArduinoCron statusArduinoCron;

  @Autowired
  private SensorService sensorService;

  /**
   * @param cron.cronExpression 0/3 * * ? * * *
   */
  @ApiOperation("Atualiza o escalonador")
  @ResponseStatus(value = OK)
  @PostMapping(path = RESCHEDULE_PATH, consumes = APPLICATION_JSON_VALUE)
  public void reschedule(@ApiParam("VO com a expressão Cron") @Valid @RequestBody CronVO cron) {
    statusArduinoCron.reschedule(cron.getCronExpression());
    Sensor sensor = sensorService.findById(sensorId);
    sensor.setStatusArduinoCron(cron.getCronExpression());
    sensorService.save(sensor);
  }

  @ApiOperation("Pausa o escalonador")
  @ResponseStatus(value = OK)
  @GetMapping(PAUSE_PATH)
  public void pause() {
    statusArduinoCron.pause();
  }

  @ApiOperation("Reinicializa o escalonador")
  @ResponseStatus(value = OK)
  @GetMapping(RESUME_PATH)
  public void resume() {
    statusArduinoCron.resume();
  }

}
