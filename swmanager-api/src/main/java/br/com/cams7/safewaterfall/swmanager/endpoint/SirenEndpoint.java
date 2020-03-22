/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.endpoint;

import static br.com.cams7.safewaterfall.swmanager.endpoint.SirenEndpoint.SIREN_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.cams7.safewaterfall.swmanager.service.SirenService;
import io.swagger.annotations.Api;

/**
 * @author CAMs7
 *
 */
@Api("Endpoint utilizado para executar as funcionalidades da Sirene.")
@RestController
@RequestMapping(path = SIREN_PATH, produces = APPLICATION_JSON_UTF8_VALUE)
public class SirenEndpoint {

  public static final String SIREN_PATH = "/siren";

  @Autowired
  private SirenService service;

}
