/**
 * 
 */
package br.com.cams7.safewaterfall.swsiren.service;

import br.com.cams7.safewaterfall.swsiren.model.Siren;

/**
 * @author CAMs7
 *
 */
public interface SirenService {

  /**
   * Salva ou atualiza os dados da sirene
   * 
   * @param siren Sirene
   * @return
   */
  Siren save(Siren siren);

  /**
   * Buscar a sirene pelo ID
   * 
   * @param id ID do sirene
   * @return Sirene
   */
  Siren findById(String id);

}
