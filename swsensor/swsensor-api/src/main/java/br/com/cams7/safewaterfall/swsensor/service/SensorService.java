/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.service;

import br.com.cams7.safewaterfall.swsensor.model.SensorEntity;

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
   * Buscar a expressão pelo ID do sensor
   * 
   * @param id ID do sensor
   * @return Expressão Cron para as consulta da distancia enviadas pelo arduino
   */
  String findStatusArduinoCronById(Long id);

  /**
   * Buscar a expressão pelo ID do sensor
   * 
   * @param id ID do sensor
   * @return Expressão Cron para envio do status do sensor
   */
  String findSendStatusMessageCronById(Long id);

  /**
   * Buscar a expressão pelo ID do sensor
   * 
   * @param id ID do sensor
   * @return Expressão Cron para envio de alertas quando quando uma distancia minima foi alcançada
   */
  String findSendAlertMessageCronById(Long id);

  /**
   * Buscar a distancia minima pelo ID do sensor
   * 
   * @param id ID do sensor
   * @return Distancia minima permitida
   */
  Short findMinimumAllowedDistanceById(Long id);
}
