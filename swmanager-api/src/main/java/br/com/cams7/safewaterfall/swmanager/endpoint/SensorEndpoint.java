package br.com.cams7.safewaterfall.swmanager.endpoint;

import static br.com.cams7.safewaterfall.swmanager.endpoint.SensorEndpoint.SENSOR_PATH;
import static br.com.cams7.safewaterfall.swmanager.model.SensorEntity.getSensor;
import static br.com.cams7.safewaterfall.swmanager.model.SensorEntity.setSensor;
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
import br.com.cams7.safewaterfall.swmanager.endpoint.common.BaseEndpoint;
import br.com.cams7.safewaterfall.swmanager.model.SensorEntity;
import br.com.cams7.safewaterfall.swmanager.service.SensorService;
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
public class SensorEndpoint extends BaseEndpoint<AppSensorVO> {

  public static final String SENSOR_PATH = "/sensor";

  @Autowired
  private SensorService sensorService;

  @ApiOperation("Salva ou atualiza os dados do sensor")
  @ResponseStatus(value = OK)
  @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
  public void save(@ApiParam("Sensor") @Valid @RequestBody SensorEntity sensor) {
    sensorService.save(sensor);
  }

  @ApiOperation("Buscar o sensor pelo ID")
  @GetMapping(path = "{id}")
  @ResponseStatus(value = OK)
  public SensorEntity findById(@ApiParam("ID do sensor") @PathVariable Long id) {
    SensorEntity sensor = sensorService.findById(id);
    return sensor;
  }

  @ApiOperation("Sincroniza o sensor pelo ID")
  @ResponseStatus(value = OK)
  @GetMapping(path = "synchronize_sensor/{id}")
  public void synchronizeSensor(@ApiParam("ID do sensor") @PathVariable Long id) {
    SensorEntity sensor = sensorService.findById(id);
    final String SENSOR_URL = sensor.getSensorAddress();

    AppSensorVO appSensor = getSensor(sensor);

    changeValue(String.format("%s/sensor", SENSOR_URL), appSensor);
  }

  @ApiOperation("Carregar os dados do sensor pelo ID")
  @ResponseStatus(value = OK)
  @GetMapping(path = "load_sensor/{id}")
  public void loadSensor(@ApiParam("ID do sensor") @PathVariable Long id) {
    SensorEntity sensor = sensorService.findById(id);
    final String SENSOR_URL = sensor.getSensorAddress();

    AppSensorVO appSensor = getValue(String.format("%s/sensor/%d", SENSOR_URL, id));
    setSensor(sensor, appSensor);

    sensorService.save(sensor);
  }

}
