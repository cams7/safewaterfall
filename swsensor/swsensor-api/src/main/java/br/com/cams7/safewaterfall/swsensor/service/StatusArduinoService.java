/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.service;

import br.com.cams7.safewaterfall.arduino.ArduinoService;

/**
 * @author CAMs7
 *
 */
public interface StatusArduinoService extends ArduinoService {

  /**
   * Carrega a distancia medida (em milimetros) pelo sensor na memoria
   * 
   */
  void loadDistance();

  /**
   * Busca a distancia (em milimetros) medida pelo sensor que foi carregada previamente,
   * 
   * Obs.: Os dados sao recebidos pela SERIAL do ARDUINO
   */
  short getDistance();

  void runTask();

}
