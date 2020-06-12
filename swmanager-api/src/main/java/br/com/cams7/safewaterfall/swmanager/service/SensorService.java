/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.service;

import br.com.cams7.safewaterfall.swmanager.model.SensorEntity;

/**
 * @author CAMs7
 *
 */
public interface SensorService {

  /**
   * Cadastra um novo sensor
   * 
   * @param sensor Sensor
   * @return Sensor
   */
  SensorEntity create(SensorEntity sensor);

  /**
   * Atualiza os dados do sensor
   * 
   * @param sensor Sensor
   * @return Sensor
   */
  SensorEntity update(SensorEntity sensor);

  /**
   * Exclui os dados do sensor pelo ID
   * 
   * @param id ID do sensor
   */
  void delete(Long id);

  /**
   * Buscar o sensor pelo ID
   * 
   * @param id ID do sensor
   * @return Sensor
   */
  SensorEntity findById(Long id);

}
