/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.service;

import br.com.cams7.safewaterfall.swmanager.model.vo.AppSirenVO;

/**
 * @author CAMs7
 *
 */
public interface AppSirenService {

  AppSirenVO save(AppSirenVO siren);

  AppSirenVO findById(String id);

  boolean existsById(String id);

  // Iterable<AppSirenVO> findBySirenId(String sirenId);

  Iterable<AppSirenVO> findAll();

}
