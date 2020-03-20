/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.endpoint;

import static br.com.cams7.safewaterfall.swsensor.endpoint.ArduinoEndpoint.ARDUINO_PATH;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.cams7.safewaterfall.common.model.PinPK;
import br.com.cams7.safewaterfall.swsensor.arduino.model.vo.Arduino.ArduinoEvent;
import br.com.cams7.safewaterfall.swsensor.arduino.model.vo.ArduinoPin.ArduinoPinType;
import br.com.cams7.safewaterfall.swsensor.service.AppArduinoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author CAMs7
 *
 */
@Api("Endpoint utilizado para executar as funcionalidades do Arduino.")
@RestController
@RequestMapping(path = ARDUINO_PATH, produces = APPLICATION_JSON_UTF8_VALUE)
public class ArduinoEndpoint {

	public static final String ARDUINO_PATH = "/arduino";

	@Autowired
	private AppArduinoService service;

	@ApiOperation("Carrega a distancia medida (em milimetros) pelo sensor na memoria")
	@GetMapping(path = "/carregar_distancia")
	@ResponseStatus(value = OK)
	public void carregarDistancia() {
		PinPK pino = new PinPK(ArduinoPinType.DIGITAL, (short) 8);
		service.carregarDistancia(pino);
	}

	@ApiOperation("Busca a distancia (em milimetros) medida pelo sensor que foi carregada previamente")
	@GetMapping(path = "/buscar_distancia")
	@ResponseStatus(value = OK)
	public Short buscarDistancia() {
		ArduinoEvent evento = ArduinoEvent.MESSAGE;
		PinPK pino = new PinPK(ArduinoPinType.DIGITAL, (short) 8);
		Short distancia = service.buscarDistancia(pino, evento);
		return distancia;
	}

}
