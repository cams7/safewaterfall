/**
 * 
 */
package br.com.cams7.safewaterfall.swsiren.endpoint;

import static br.com.cams7.safewaterfall.swsiren.endpoint.StatusArduinoEndpoint.STATUS_ARDUINO_PATH;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import br.com.cams7.safewaterfall.swsiren.service.StatusArduinoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author CAMs7
 *
 */
@Api("Endpoint utilizado para executar as funcionalidades do Arduino.")
@RestController
@RequestMapping(path = STATUS_ARDUINO_PATH, produces = APPLICATION_JSON_UTF8_VALUE)
public class StatusArduinoEndpoint {

  public static final String STATUS_ARDUINO_PATH = "/status_arduino";

  @Autowired
  private StatusArduinoService service;

  @ApiOperation("Aciona a sirene")
  @GetMapping(path = "acionar_sirene")
  @ResponseStatus(value = OK)
  public void acionarSirene() {
    service.alteraEstadoSirene(true);
  }

  @ApiOperation("Desliga a sirena")
  @GetMapping(path = "desligar_sirene")
  @ResponseStatus(value = OK)
  public void desligarSirene() {
    service.alteraEstadoSirene(false);
  }

}
