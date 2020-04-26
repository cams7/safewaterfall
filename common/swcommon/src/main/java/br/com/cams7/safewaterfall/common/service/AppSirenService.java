/**
 * 
 */
package br.com.cams7.safewaterfall.common.service;

import br.com.cams7.safewaterfall.common.model.vo.AppSirenVO;

/**
 * @author CAMs7
 *
 */
public interface AppSirenService {

  AppSirenVO save(AppSirenVO siren);

  AppSirenVO findById(Long id);

  boolean existsById(Long id);
}