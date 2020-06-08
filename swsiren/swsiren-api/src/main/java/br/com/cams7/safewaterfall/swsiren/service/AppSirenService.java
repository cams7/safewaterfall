/**
 * 
 */
package br.com.cams7.safewaterfall.swsiren.service;

import br.com.cams7.safewaterfall.swsiren.model.vo.AppSirenVO;

/**
 * @author CAMs7
 *
 */
public interface AppSirenService {

  AppSirenVO save(AppSirenVO siren);

  AppSirenVO findById(String id);

  boolean existsById(String id);

}
