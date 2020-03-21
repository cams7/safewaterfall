/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.service;

import br.com.cams7.safewaterfall.arduino.ArduinoService;
import br.com.cams7.safewaterfall.arduino.model.vo.Arduino.ArduinoEvent;
import br.com.cams7.safewaterfall.common.model.PinPK;

/**
 * @author CAMs7
 *
 */
public interface AppArduinoService extends ArduinoService {

  /**
   * Carrega a distancia medida (em milimetros) pelo sensor na memoria
   * 
   * @param PINO de Eco - Numero do PINO DIGITAL
   */
  void carregarDistancia(PinPK pino);

  /**
   * Busca a distancia (em milimetros) medida pelo sensor que foi carregada previamente,
   * 
   * Obs.: Os dados sao recebidos pela SERIAL do ARDUINO
   * 
   * @param PINO de Eco - Numero do PINO DIGITAL
   * @return Distancia medida pelo sensor
   */
  Short buscarDistancia(PinPK pino, ArduinoEvent arduinoEvent);

}
