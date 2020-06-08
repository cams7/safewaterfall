/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.service;

import br.com.cams7.safewaterfall.swmanager.model.vo.AppSirenAddressVO;

/**
 * @author CAMs7
 *
 */
public interface AppSirenAddressService {

  AppSirenAddressVO save(AppSirenAddressVO sirenAddress);

  AppSirenAddressVO findById(String id);

  boolean existsById(String id);

}
