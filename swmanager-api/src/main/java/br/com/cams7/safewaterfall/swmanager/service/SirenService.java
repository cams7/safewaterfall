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
   * @param siren Siren
   */
  SirenEntity save(SirenEntity siren);

  /**
   * Buscar a sirene pelo ID
   * 
   * @param id ID da sirene
   * @return Siren
   */
  SirenEntity findById(Long id);

}
