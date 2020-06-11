/**
 * 
 */
package br.com.cams7.safewaterfall.swsiren.endpoint;

import static br.com.cams7.safewaterfall.swsiren.endpoint.SirenEndpoint.SIREN_PATH;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import br.com.cams7.safewaterfall.common.error.AppResourceNotFoundException;
import br.com.cams7.safewaterfall.swsiren.model.Siren;
import br.com.cams7.safewaterfall.swsiren.service.SirenService;
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
@RequestMapping(path = SIREN_PATH, produces = APPLICATION_JSON_VALUE)
public class SirenEndpoint {

  public static final String SIREN_PATH = "/siren";

  @Value("${SIREN_ID}")
  private String sirenId;

  @Autowired
  private StatusArduinoService arduinoService;

  @Autowired
  private SirenService sirenService;

  @ApiOperation("Alterar o estado da sirene")
  @GetMapping(path = "change_status/{active}")
  @ResponseStatus(value = OK)
  public void changeStatus(@ApiParam("Sirene esta ativa") @PathVariable boolean active) {
    arduinoService.changeSirenStatus(active);
    Siren siren = sirenService.findById(sirenId);
    if (active != siren.isActive()) {
      siren.setActive(active);
      sirenService.save(siren);
    }
  }

  @ApiOperation("Salva ou atualiza os dados da sirene")
  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(value = OK)
  public Siren save(@ApiParam("Sirene") @Valid @RequestBody Siren siren) {
    String id = siren.getId();
    if (!sirenId.equals(id))
      throw new AppResourceNotFoundException(String.format("O ID %d não corresponde ao ID da sirene", id));

    Siren currentSiren = sirenService.findById(sirenId);
    if (currentSiren.isActive() != siren.isActive())
      arduinoService.changeSirenStatus(siren.isActive());

    return sirenService.save(siren);
  }

  @ApiOperation("Buscar a sirene pelo ID")
  @GetMapping
  @ResponseStatus(value = OK)
  public Siren getSiren() {
    Siren siren = sirenService.findById(sirenId);
    return siren;
  }

}
