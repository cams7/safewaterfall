/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.endpoint;

import static br.com.cams7.safewaterfall.swmanager.endpoint.SirenEndpoint.SIREN_PATH;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
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
import br.com.cams7.safewaterfall.common.model.vo.AppSensorVO;
import br.com.cams7.safewaterfall.swmanager.endpoint.common.BaseEndpoint;
import br.com.cams7.safewaterfall.swmanager.model.SirenEntity;
import br.com.cams7.safewaterfall.swmanager.model.vo.AppSirenAddressVO;
import br.com.cams7.safewaterfall.swmanager.model.vo.AppSirenVO;
import br.com.cams7.safewaterfall.swmanager.service.AppSirenAddressService;
import br.com.cams7.safewaterfall.swmanager.service.AppSirenService;
import br.com.cams7.safewaterfall.swmanager.service.SensorService;
import br.com.cams7.safewaterfall.swmanager.service.SirenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author CAMs7
 *
 */
@Api("Endpoint utilizado para executar as funcionalidades do sirene.")
@RestController
@RequestMapping(path = SIREN_PATH, produces = APPLICATION_JSON_UTF8_VALUE)
public class SirenEndpoint extends BaseEndpoint<AppSirenVO> {

  public static final String SIREN_PATH = "/siren";

  @Autowired
  private AppSirenService appSirenService;

  @Autowired
  private AppSirenAddressService appSirenAddressService;

  @Autowired
  private SensorService sensorService;

  @Autowired
  private SirenService sirenService;

  @ApiOperation("Salva ou atualiza os dados da sirene")
  @ResponseStatus(value = OK)
  @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
  public void save(@ApiParam("Sirene") @Valid @RequestBody SirenEntity siren) {
    sirenService.save(siren);
  }

  @ApiOperation("Buscar o sirene pelo ID")
  @GetMapping(path = "{id}")
  @ResponseStatus(value = OK)
  public SirenEntity findById(@ApiParam("ID da sirene") @PathVariable Long id) {
    SirenEntity siren = sirenService.findById(id);
    return siren;
  }

  @ApiOperation("Atualiza o estado da sirene")
  @PostMapping(path = "change_status", consumes = APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(value = OK)
  public void changeStatus(@ApiParam("Sensor") @Valid @RequestBody AppSensorVO appSensor) {
    String sensorId = appSensor.getId();
    short distance = appSensor.getDistance();
    boolean active = distance < appSensor.getMinimumAllowedDistance();
    boolean changeSirenStatus = false;

    final String DEVICE_ID;
    final String SIREN_URL;

    boolean isNewSiren = false;

    AppSirenVO appSiren = null;
    if (appSirenService.existsById(sensorId))
      appSiren = appSirenService.findById(sensorId);

    if (appSiren != null) {
      DEVICE_ID = appSiren.getSirenId();
      AppSirenAddressVO sirenAddress = appSirenAddressService.findById(DEVICE_ID);
      SIREN_URL = sirenAddress.getAddress();
    } else {
      SirenEntity siren = sensorService.findSirenByDeviceId(appSensor.getId());
      DEVICE_ID = siren.getDeviceId();
      SIREN_URL = siren.getSirenAddress();
      appSiren = new AppSirenVO(sensorId, DEVICE_ID);
      isNewSiren = true;
    }

    List<AppSirenVO> sirens = StreamSupport.stream(appSirenService.findAll().spliterator(), false).filter(
        s -> DEVICE_ID.equals(s.getSirenId())).collect(Collectors.toList());
    changeSirenStatus = isNewSiren && sirens.stream().noneMatch(s -> !sensorId.equals(s.getId()));

    if (!changeSirenStatus) {
      boolean isAnotherActiveSensor = !active && sirens.stream().anyMatch(s -> s.isActive() && !sensorId.equals(s
          .getId()));
      if (!isAnotherActiveSensor)
        changeSirenStatus = active != appSiren.isActive();
    }

    if (changeSirenStatus) {
      appSiren.setActive(active);

      changeValue(String.format("%s%s/change_status/%b", SIREN_URL, SIREN_PATH, active));
    }

    if (isNewSiren || sirens.stream().anyMatch(s -> sensorId.equals(s.getId()) && active != s.isActive())) {
      appSirenService.save(appSiren);
      if (isNewSiren) {
        AppSirenAddressVO sirenAddress = new AppSirenAddressVO(DEVICE_ID);
        sirenAddress.setAddress(SIREN_URL);
        appSirenAddressService.save(sirenAddress);
      }
    }
  }

}
