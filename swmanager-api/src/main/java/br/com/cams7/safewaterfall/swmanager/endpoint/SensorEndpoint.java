package br.com.cams7.safewaterfall.swmanager.endpoint;

import static br.com.cams7.safewaterfall.swmanager.endpoint.SensorEndpoint.SENSOR_PATH;
import static br.com.cams7.safewaterfall.swmanager.model.SensorEntity.getSensor;
import static br.com.cams7.safewaterfall.swmanager.model.SensorEntity.setSensor;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import br.com.cams7.safewaterfall.common.model.Sensor;
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
@RequestMapping(path = SENSOR_PATH, produces = APPLICATION_JSON_VALUE)
public class SensorEndpoint extends BaseEndpoint<Sensor> {

  public static final String SENSOR_PATH = "/sensor";

  @Autowired
  private SensorService sensorService;

  @ApiOperation("Cadastra os dados do sensor")
  @ResponseStatus(value = CREATED)
  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  public SensorEntity create(@ApiParam("Sensor") @Valid @RequestBody SensorEntity sensor) {
    return sensorService.create(sensor);
  }

  @ApiOperation("Atualiza os dados do sensor")
  @ResponseStatus(value = OK)
  @PutMapping(consumes = APPLICATION_JSON_VALUE)
  public SensorEntity update(@ApiParam("Sensor") @Valid @RequestBody SensorEntity sensor) {
    return sensorService.update(sensor);
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
    SensorEntity sensorEntity = sensorService.findById(id);
    final String SENSOR_URL = sensorEntity.getSensorAddress();

    Sensor sensor = getSensor(sensorEntity);

    changeValue(String.format("%s%s", SENSOR_URL, SENSOR_PATH), sensor);
  }

  @ApiOperation("Carregar os dados do sensor pelo ID")
  @ResponseStatus(value = OK)
  @GetMapping(path = "load_sensor/{id}")
  public void loadSensor(@ApiParam("ID do sensor") @PathVariable Long id) {
    SensorEntity sensorEntity = sensorService.findById(id);
    final String SENSOR_URL = sensorEntity.getSensorAddress();

    Sensor sensor = getValue(String.format("%s/sensor", SENSOR_URL));
    setSensor(sensorEntity, sensor);

    sensorService.update(sensorEntity);
  }

}
