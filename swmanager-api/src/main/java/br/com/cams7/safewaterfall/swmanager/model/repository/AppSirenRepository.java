/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import br.com.cams7.safewaterfall.swmanager.model.vo.AppSirenVO;

/**
 * @author CAMs7
 *
 */
@Repository
public interface AppSirenRepository extends CrudRepository<AppSirenVO, String> {
}
