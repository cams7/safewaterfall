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
   * Cadastra uma nova sirene
   * 
   * @param siren Sirene
   * @return Sirene
   */
  SirenEntity create(SirenEntity siren);

  /**
   * Atualiza os dados da sirene
   * 
   * @param siren Sirene
   * @return Sirene
   */
  SirenEntity update(SirenEntity siren);

  /**
   * Exclui os dados da sirene pelo ID
   * 
   * @param id ID do sirene
   */
  void delete(Long id);

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
