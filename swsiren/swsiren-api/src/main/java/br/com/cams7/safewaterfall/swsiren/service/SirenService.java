/**
 * 
 */
package br.com.cams7.safewaterfall.swsiren.service;

import br.com.cams7.safewaterfall.swsiren.model.vo.SirenVO;

/**
 * @author CAMs7
 *
 */
public interface SirenService {

  SirenVO save(SirenVO siren);

  SirenVO findById(String id);

  boolean existsById(String id);

}
