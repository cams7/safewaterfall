/**
 * 
 */
package br.com.cams7.safewaterfall.common.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import br.com.cams7.safewaterfall.common.model.vo.AppSirenAddressVO;

/**
 * @author CAMs7
 *
 */
@Repository
public interface AppSirenAddressRepository extends CrudRepository<AppSirenAddressVO, String> {
}
