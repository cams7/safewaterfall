/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.endpoint;

import static br.com.cams7.safewaterfall.swmanager.endpoint.SirenEndpoint.SIREN_PATH;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import br.com.cams7.safewaterfall.common.model.Sensor;
import br.com.cams7.safewaterfall.swmanager.endpoint.common.BaseEndpoint;
import br.com.cams7.safewaterfall.swmanager.model.SirenEntity;
import br.com.cams7.safewaterfall.swmanager.model.vo.AppSirenVO;
import br.com.cams7.safewaterfall.swmanager.model.vo.AppSensorVO;
import br.com.cams7.safewaterfall.swmanager.service.AppSirenService;
import br.com.cams7.safewaterfall.swmanager.service.AppSensorService;
import br.com.cams7.safewaterfall.swmanager.service.SirenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author CAMs7
 *
 */
@Api("Endpoint utilizado para executar as funcionalidades da sirene.")
@RestController
@RequestMapping(path = SIREN_PATH, produces = APPLICATION_JSON_VALUE)
public class SirenEndpoint extends BaseEndpoint<AppSensorVO> {

  public static final String SIREN_PATH = "/siren";

  @Autowired
  private SirenService sirenService;

  @Autowired
  private AppSensorService appSensorService;

  @Autowired
  private AppSirenService appSirenService;

  @ApiOperation("Salva ou atualiza os dados da sirene")
  @ResponseStatus(value = OK)
  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  public SirenEntity save(@ApiParam("Sirene") @Valid @RequestBody SirenEntity siren) {
    return sirenService.save(siren);
  }

  @ApiOperation("Buscar o sirene pelo ID")
  @GetMapping(path = "{id}")
  @ResponseStatus(value = OK)
  public SirenEntity findById(@ApiParam("ID da sirene") @PathVariable Long id) {
    SirenEntity siren = sirenService.findById(id);
    return siren;
  }

  @ApiOperation("Atualiza o estado da sirene")
  @PostMapping(path = "change_status", consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(value = OK)
  public void changeStatus(@ApiParam("Sensor") @Valid @RequestBody Sensor sensor) {
    String sensorId = sensor.getId();
    short distance = sensor.getDistance();
    boolean active = distance < sensor.getMinimumAllowedDistance();
    boolean changeSensorStatus = false;

    final String DEVICE_ID;
    final String SIREN_URL;

    boolean isNewSiren = false;

    AppSensorVO appSensor = null;
    if (appSensorService.existsById(sensorId))
      appSensor = appSensorService.findById(sensorId);

    if (appSensor != null) {
      DEVICE_ID = appSensor.getSirenId();
      AppSirenVO appSiren = appSirenService.findById(DEVICE_ID);
      SIREN_URL = appSiren.getAddress();
    } else {
      SirenEntity sirenEntity = sirenService.findBySensorDeviceId(sensor.getId());
      DEVICE_ID = sirenEntity.getDeviceId();
      SIREN_URL = sirenEntity.getSirenAddress();
      appSensor = new AppSensorVO(sensorId, DEVICE_ID);
      isNewSiren = true;
    }

    List<AppSensorVO> sensors = StreamSupport.stream(appSensorService.findAll().spliterator(), false).filter(
        s -> DEVICE_ID.equals(s.getSirenId())).collect(Collectors.toList());
    changeSensorStatus = isNewSiren && sensors.stream().noneMatch(s -> !sensorId.equals(s.getId()));

    if (!changeSensorStatus) {
      boolean isAnotherActiveSensor = !active && sensors.stream().anyMatch(s -> s.isActive() && !sensorId.equals(s
          .getId()));
      if (!isAnotherActiveSensor)
        changeSensorStatus = active != appSensor.isActive();
    }

    if (changeSensorStatus) {
      appSensor.setActive(active);

      changeValue(String.format("%s%s/change_status/%b", SIREN_URL, SIREN_PATH, active));
    }

    if (isNewSiren || sensors.stream().anyMatch(s -> sensorId.equals(s.getId()) && active != s.isActive())) {
      appSensorService.save(appSensor);
      if (isNewSiren) {
        AppSirenVO appSiren = new AppSirenVO(DEVICE_ID);
        appSiren.setAddress(SIREN_URL);
        appSirenService.save(appSiren);
      }
    }
  }

}
