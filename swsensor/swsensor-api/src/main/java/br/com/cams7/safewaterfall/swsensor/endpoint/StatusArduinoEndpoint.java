/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.endpoint;

import static br.com.cams7.safewaterfall.swsensor.endpoint.StatusArduinoEndpoint.STATUS_ARDUINO_PATH;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import br.com.cams7.safewaterfall.swsensor.service.StatusArduinoService;
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

  @ApiOperation("Carrega a distancia medida (em milimetros) pelo sensor na memoria")
  @GetMapping(path = "/carregar_distancia")
  @ResponseStatus(value = OK)
  public void loadDistance() {
    service.loadDistance();
  }

  @ApiOperation("Busca a distancia (em milimetros) medida pelo sensor que foi carregada previamente")
  @GetMapping(path = "/buscar_distancia")
  @ResponseStatus(value = OK)
  public short getDistance() {
    short distancia = service.getDistance();
    return distancia;
  }

}
