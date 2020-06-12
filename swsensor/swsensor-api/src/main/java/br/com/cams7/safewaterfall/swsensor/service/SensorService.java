/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.service;

import br.com.cams7.safewaterfall.common.model.Sensor;

/**
 * @author CAMs7
 *
 */
public interface SensorService {

  /**
   * cadastra os dados do sensor
   * 
   * @param sensor Sensor
   * @return Sensor
   */
  Sensor create(Sensor sensor);

  /**
   * Salva ou atualiza os dados do sensor
   * 
   * @param sensor Sensor
   * @return Sensor
   */
  Sensor update(Sensor sensor);

  /**
   * Buscar o sensor pelo ID
   * 
   * @param id ID do sensor
   * @return Sensor
   */
  Sensor findById(String id);

  /**
   * Retorna verdadeiro caso o sensor tenha sido cadastrado
   * 
   * @param id ID do sensor
   * @return Sensor foi cadastrado
   */
  boolean existsById(String id);

}
