/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.service;

import br.com.cams7.safewaterfall.common.model.vo.SensorVO;

/**
 * @author CAMs7
 *
 */
public interface SensorService {

  /**
   * @param sensor
   */
  void atualizarSensor(SensorVO sensor);
}
