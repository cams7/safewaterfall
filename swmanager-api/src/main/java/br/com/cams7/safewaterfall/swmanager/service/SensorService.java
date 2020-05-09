/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.service;

import br.com.cams7.safewaterfall.swmanager.model.SensorEntity;
import br.com.cams7.safewaterfall.swmanager.model.SirenEntity;

/**
 * @author CAMs7
 *
 */
public interface SensorService {

  /**
   * Salva ou atualiza os dados do sensor
   * 
   * @param sensor Sensor
   */
  SensorEntity save(SensorEntity sensor);

  /**
   * Buscar o sensor pelo ID
   * 
   * @param id ID do sensor
   * @return Sensor
   */
  SensorEntity findById(Long id);

  /**
   * Buscar o ID da sirene pelo ID do sensor
   * 
   * @param id ID da sirene
   * @return ID da sirene
   */
  SirenEntity findSirenById(Long id);

}
