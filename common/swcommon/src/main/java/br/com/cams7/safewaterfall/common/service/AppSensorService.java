/**
 * 
 */
package br.com.cams7.safewaterfall.common.service;

import br.com.cams7.safewaterfall.common.model.vo.AppSensorVO;

/**
 * @author CAMs7
 *
 */
public interface AppSensorService {

  AppSensorVO save(AppSensorVO sensor);

  AppSensorVO findById(String id);

  boolean existsById(String id);
}
