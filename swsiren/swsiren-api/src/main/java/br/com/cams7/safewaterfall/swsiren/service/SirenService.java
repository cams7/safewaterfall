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
   * Salva os dados da sirene
   * 
   * @param siren Sirene
   * @return Sirene
   */
  Siren create(Siren siren);

  /**
   * Atualiza os dados da sirene
   * 
   * @param siren Sirene
   * @return Sirene
   */
  Siren update(Siren siren);

  /**
   * Buscar a sirene pelo ID
   * 
   * @param id ID do sirene
   * @return Sirene
   */
  Siren findById(String id);

  /**
   * Retorna verdadeiro caso a sirene tenha sido cadastrada
   * 
   * @param id ID da sirene
   * @return Sirene foi cadastrada
   */
  boolean existsById(String id);

}
