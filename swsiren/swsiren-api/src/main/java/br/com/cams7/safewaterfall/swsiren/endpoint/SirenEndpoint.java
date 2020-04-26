/**
 * 
 */
package br.com.cams7.safewaterfall.swsiren.endpoint;

import static br.com.cams7.safewaterfall.swsiren.endpoint.SirenEndpoint.STATUS_ARDUINO_PATH;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import br.com.cams7.safewaterfall.common.model.vo.AppSirenVO;
import br.com.cams7.safewaterfall.common.service.AppSirenService;
import br.com.cams7.safewaterfall.swsiren.service.StatusArduinoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author CAMs7
 *
 */
@Api("Endpoint utilizado para executar as funcionalidades do Arduino.")
@RestController
@RequestMapping(path = STATUS_ARDUINO_PATH, produces = APPLICATION_JSON_UTF8_VALUE)
public class SirenEndpoint {

  public static final String STATUS_ARDUINO_PATH = "/siren";

  @Autowired
  private StatusArduinoService arduinoService;

  @Autowired
  private AppSirenService appSirenService;

  @ApiOperation("Alterar o estado da sirene")
  @PostMapping(path = "alterar_estado")
  @ResponseStatus(value = OK)
  public void alteraEstado(@ApiParam("Sirene") @RequestBody AppSirenVO siren) {
    arduinoService.alteraEstadoSirene(siren.isActive());
    appSirenService.save(siren);
  }

}
