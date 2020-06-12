/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.service;

import br.com.cams7.safewaterfall.swmanager.model.vo.AppSensorVO;

/**
 * @author CAMs7
 *
 */
public interface AppSensorService {

  AppSensorVO save(AppSensorVO sensor);

  AppSensorVO findById(String id);

  boolean existsById(String id);

  Iterable<AppSensorVO> findAll();

}
