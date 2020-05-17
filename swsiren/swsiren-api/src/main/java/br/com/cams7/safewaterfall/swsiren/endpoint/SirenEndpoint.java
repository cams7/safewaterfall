/**
 * 
 */
package br.com.cams7.safewaterfall.swsiren.endpoint;

import static br.com.cams7.safewaterfall.swsiren.endpoint.SirenEndpoint.SIREN_PATH;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import br.com.cams7.safewaterfall.common.error.AppResourceNotFoundException;
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
@Api("Endpoint utilizado para executar as funcionalidades da sirene.")
@RestController
@RequestMapping(path = SIREN_PATH, produces = APPLICATION_JSON_UTF8_VALUE)
public class SirenEndpoint {

  public static final String SIREN_PATH = "/siren";

  @Value("${SIREN_ID}")
  private String sirenId;

  @Autowired
  private StatusArduinoService arduinoService;

  @Autowired
  private AppSirenService appSirenService;

  @ApiOperation("Alterar o estado da sirene")
  @PostMapping(path = "change_status", consumes = APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(value = OK)
  public void changeStatus(@ApiParam("Sirene") @Valid @RequestBody AppSirenVO appSiren) {
    String id = appSiren.getId();
    if (!sirenId.equals(id))
      throw new AppResourceNotFoundException(String.format("O ID %d não corresponde ao ID da sirene", id));

    appSirenService.save(appSiren);
    arduinoService.changeSirenStatus(appSiren.isActive());
  }

}
