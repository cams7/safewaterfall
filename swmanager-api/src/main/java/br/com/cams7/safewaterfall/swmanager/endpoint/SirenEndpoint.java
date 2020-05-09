/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.endpoint;

import static br.com.cams7.safewaterfall.swmanager.endpoint.SirenEndpoint.SIREN_PATH;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestOperations;
import br.com.cams7.safewaterfall.common.model.vo.AppSensorVO;
import br.com.cams7.safewaterfall.common.model.vo.AppSirenVO;
import br.com.cams7.safewaterfall.common.service.AppSensorService;
import br.com.cams7.safewaterfall.common.service.AppSirenService;
import br.com.cams7.safewaterfall.swmanager.model.SirenEntity;
import br.com.cams7.safewaterfall.swmanager.service.SensorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author CAMs7
 *
 */
@Slf4j
@Api("Endpoint utilizado para executar as funcionalidades do Sensor.")
@RestController
@RequestMapping(path = SIREN_PATH, produces = APPLICATION_JSON_UTF8_VALUE)
public class SirenEndpoint {

  public static final String SIREN_PATH = "/siren";

  @Autowired
  private AppSensorService appSensorService;

  @Autowired
  private AppSirenService appSirenService;

  @Autowired
  private SensorService sensorService;

  @Autowired
  private RestOperations restTemplate;

  @ApiOperation("Atualiza o estado da sirene")
  @PostMapping(path = "change_status")
  @ResponseStatus(value = OK)
  public void changeStatus(@ApiParam("Sensor") @RequestBody AppSensorVO sensor) {
    short distance = sensor.getDistance();
    boolean active = distance < sensor.getMinimumAllowedDistance();
    boolean changeSirenStatus = false;

    SirenEntity siren = sensorService.findSirenById(sensor.getId());
    final long SIREN_ID = siren.getId();
    final String SIREN_URL = siren.getSirenAddress();

    AppSirenVO appSiren;
    if (appSirenService.existsById(SIREN_ID)) {
      appSiren = appSirenService.findById(SIREN_ID);
    } else {
      appSiren = new AppSirenVO(SIREN_ID);
      changeSirenStatus = true;
    }

    if (!changeSirenStatus) {
      changeSirenStatus = active != appSiren.isActive();
    }

    if (changeSirenStatus) {
      appSiren.setActive(active);

      // setting up the request headers
      HttpHeaders requestHeaders = new HttpHeaders();
      requestHeaders.setContentType(MediaType.APPLICATION_JSON);

      // request entity is created with request body and headers
      HttpEntity<AppSirenVO> requestEntity = new HttpEntity<>(appSiren, requestHeaders);

      try {
        ResponseEntity<Void> responseEntity = restTemplate.exchange(String.format("%s/siren/change_status",
            SIREN_URL), HttpMethod.POST, requestEntity, Void.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
          log.info("Response retrieved");
        }
      } catch (ResourceAccessException e) {
        log.error(e.getMessage());
      }

      appSirenService.save(appSiren);
    }
    appSensorService.save(sensor);
  }

}
