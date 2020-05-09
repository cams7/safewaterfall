/**
 * 
 */
package br.com.cams7.safewaterfall.swsiren.service;

import br.com.cams7.safewaterfall.arduino.ArduinoService;

/**
 * @author CAMs7
 *
 */
public interface StatusArduinoService extends ArduinoService {
  /**
   * Altera o ESTADO da sirene
   *
   * @param active Esta ativa
   */
  void changeSirenStatus(boolean active);

}
