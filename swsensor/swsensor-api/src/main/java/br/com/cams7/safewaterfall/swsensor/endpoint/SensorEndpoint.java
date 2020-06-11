/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.endpoint;

import static br.com.cams7.safewaterfall.swsensor.endpoint.SensorEndpoint.SENSOR_PATH;
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
import br.com.cams7.safewaterfall.common.error.AppResourceNotFoundException;
import br.com.cams7.safewaterfall.common.model.Sensor;
import br.com.cams7.safewaterfall.swsensor.cron.StatusArduinoCron;
import br.com.cams7.safewaterfall.swsensor.cron.StatusMessageCron;
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
@RequestMapping(path = SENSOR_PATH, produces = APPLICATION_JSON_VALUE)
public class SensorEndpoint {

  public static final String SENSOR_PATH = "/sensor";

  @Value("${SENSOR_ID}")
  private String sensorId;

  @Autowired
  private StatusArduinoService arduinoService;

  @Autowired
  private SensorService sensorService;

  @Autowired
  private StatusArduinoCron statusArduinoCron;

  @Autowired
  private StatusMessageCron statusMessageCron;

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
  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(value = OK)
  public Sensor save(@ApiParam("Sensor") @Valid @RequestBody Sensor sensor) {
    String id = sensor.getId();
    if (!sensorId.equals(id))
      throw new AppResourceNotFoundException(String.format("O ID %d não corresponde ao ID do sensor", id));

    Sensor currentSensor = null;
    if (sensorService.existsById(sensorId))
      currentSensor = sensorService.findById(sensorId);

    if (currentSensor == null || !currentSensor.getStatusArduinoCron().equals(sensor.getStatusArduinoCron()))
      statusArduinoCron.reschedule(sensor.getStatusArduinoCron());

    switch (sensor.getMessageStatus()) {
      case SEND_STATUS:
        if (currentSensor == null || !currentSensor.getSendStatusMessageCron().equals(sensor
            .getSendStatusMessageCron()))
          statusMessageCron.reschedule(sensor.getSendStatusMessageCron());
        break;
      case SEND_ALERT:
        if (currentSensor == null || !currentSensor.getSendAlertMessageCron().equals(sensor
            .getSendAlertMessageCron()))
          statusMessageCron.reschedule(sensor.getSendAlertMessageCron());
        break;
      default:
        break;
    }

    return sensorService.save(sensor);
  }

  @ApiOperation("Buscar o sensor pelo ID")
  @GetMapping
  @ResponseStatus(value = OK)
  public Sensor getSensor() {
    Sensor sensor = sensorService.findById(sensorId);
    return sensor;
  }

}
