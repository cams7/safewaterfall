/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.endpoint;

import static br.com.cams7.safewaterfall.swmanager.endpoint.SirenEndpoint.SIREN_PATH;
import static org.springframework.http.HttpStatus.CREATED;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import br.com.cams7.safewaterfall.common.model.Sensor;
import br.com.cams7.safewaterfall.swmanager.endpoint.common.BaseEndpoint;
import br.com.cams7.safewaterfall.swmanager.model.SirenEntity;
import br.com.cams7.safewaterfall.swmanager.model.vo.AppSensorVO;
import br.com.cams7.safewaterfall.swmanager.model.vo.AppSirenVO;
import br.com.cams7.safewaterfall.swmanager.service.AppSensorService;
import br.com.cams7.safewaterfall.swmanager.service.AppSirenService;
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

  @ApiOperation("Cadastra os dados da sirene")
  @ResponseStatus(value = CREATED)
  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  public SirenEntity create(@ApiParam("Sirene") @Valid @RequestBody SirenEntity siren) {
    return sirenService.create(siren);
  }

  @ApiOperation("Atualiza os dados da sirene")
  @ResponseStatus(value = OK)
  @PutMapping(consumes = APPLICATION_JSON_VALUE)
  public SirenEntity update(@ApiParam("Sirene") @Valid @RequestBody SirenEntity siren) {
    return sirenService.update(siren);
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
    String sensorDeviceId = sensor.getId();
    short distance = sensor.getDistance();
    boolean active = distance < sensor.getMinimumAllowedDistance();

    final AppSensorVO appSensor;
    final AppSirenVO appSiren;

    final String sirenDeviceId;
    final String sirenUrl;

    boolean isFirstSensor = false;

    if (appSensorService.existsById(sensorDeviceId)) {
      appSensor = appSensorService.findById(sensorDeviceId);
      sirenDeviceId = appSensor.getSirenId();

      appSiren = appSirenService.findById(sirenDeviceId);
      sirenUrl = appSiren.getAddress();
    } else {
      SirenEntity sirenEntity = sirenService.findBySensorDeviceId(sensor.getId());
      sirenDeviceId = sirenEntity.getDeviceId();
      sirenUrl = sirenEntity.getSirenAddress();

      appSensor = new AppSensorVO(sensorDeviceId, sirenDeviceId);
      appSiren = new AppSirenVO(sirenDeviceId);
      appSiren.setAddress(sirenUrl);

      isFirstSensor = true;
    }

    boolean changeSirenStatus = active != appSiren.isActive();

    if (!active && changeSirenStatus) {
      List<AppSensorVO> sirenSensors = StreamSupport.stream(appSensorService.findAll().spliterator(), false)
          .filter(s -> sirenDeviceId.equals(s.getSirenId())).collect(Collectors.toList());
      changeSirenStatus = sirenSensors.stream().noneMatch(s -> s.isActive() && !sensorDeviceId.equals(s.getId()));
    }

    if (isFirstSensor || changeSirenStatus) {
      changeValue(String.format("%s%s/change_status/%b", sirenUrl, SIREN_PATH, active));
      appSiren.setActive(active);
      appSirenService.save(appSiren);
    }

    if (isFirstSensor || active != appSensor.isActive()) {
      appSensor.setActive(active);
      appSensorService.save(appSensor);
    }
  }

}
