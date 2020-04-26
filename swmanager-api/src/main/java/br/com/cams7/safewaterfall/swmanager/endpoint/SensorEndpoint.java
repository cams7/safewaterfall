/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.endpoint;

import static br.com.cams7.safewaterfall.swmanager.endpoint.SensorEndpoint.SENSOR_PATH;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping(path = SENSOR_PATH, produces = APPLICATION_JSON_UTF8_VALUE)
public class SensorEndpoint {

  private static final long SIREN_ID = 1;

  public static final String SENSOR_PATH = "/sensor";

  @Value("${SIREN_URL}")
  private String sirenUrl;

  @Autowired
  private AppSensorService appSensorService;

  @Autowired
  private AppSirenService appSirenService;

  @Autowired
  private RestOperations restTemplate;

  @ApiOperation("Atualiza a ultima leitura do sensor")
  @PostMapping(path = "atualizar_estado")
  @ResponseStatus(value = OK)
  public void atualizarEstado(@ApiParam("Sensor") @RequestBody AppSensorVO sensor) {
    short distance = sensor.getDistance();
    boolean active = distance < sensor.getMinimumAllowedDistance();
    boolean changeSirenStatus = false;

    AppSirenVO siren;
    if (appSirenService.existsById(SIREN_ID)) {
      siren = appSirenService.findById(SIREN_ID);
    } else {
      siren = new AppSirenVO(SIREN_ID);
      changeSirenStatus = true;
    }

    if (!changeSirenStatus) {
      changeSirenStatus = active != siren.isActive();
    }

    if (changeSirenStatus) {
      siren.setActive(active);

      // setting up the request headers
      HttpHeaders requestHeaders = new HttpHeaders();
      requestHeaders.setContentType(MediaType.APPLICATION_JSON);

      // request entity is created with request body and headers
      HttpEntity<AppSirenVO> requestEntity = new HttpEntity<>(siren, requestHeaders);

      try {
        ResponseEntity<Void> responseEntity = restTemplate.exchange(String.format("%s/siren/alterar_estado",
            sirenUrl), HttpMethod.POST, requestEntity, Void.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
          log.info("Response retrieved");
        }
      } catch (ResourceAccessException e) {
        log.error(e.getMessage());
      }

      appSirenService.save(siren);
    }
    appSensorService.save(sensor);
  }

}
