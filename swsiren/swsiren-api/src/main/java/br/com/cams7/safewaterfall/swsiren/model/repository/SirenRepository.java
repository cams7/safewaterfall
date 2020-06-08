/**
 * 
 */
package br.com.cams7.safewaterfall.swsiren.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import br.com.cams7.safewaterfall.swsiren.model.vo.SirenVO;

/**
 * @author CAMs7
 *
 */
@Repository
public interface SirenRepository extends CrudRepository<SirenVO, String> {
}
