/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.service;

import br.com.cams7.safewaterfall.swmanager.model.SirenEntity;

/**
 * @author CAMs7
 *
 */
public interface SirenService {

  /**
   * Salva ou atualiza os dados da sirene
   * 
   * @param siren Sirene
   */
  SirenEntity save(SirenEntity siren);

  /**
   * Buscar a sirene pelo ID
   * 
   * @param id ID da sirene
   * @return Sirene
   */
  SirenEntity findById(Long id);

  /**
   * Buscar a sirene pelo ID do dispositivo
   * 
   * @param deviceId ID do dispositivo
   * @return Sirene
   */
  SirenEntity findBySensorDeviceId(String deviceId);

}
