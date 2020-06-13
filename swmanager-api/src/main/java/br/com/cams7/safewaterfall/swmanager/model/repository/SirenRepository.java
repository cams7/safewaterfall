/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.model.repository;

// import static br.com.cams7.safewaterfall.swmanager.model.SirenEntity.WITH_SENSORS;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.com.cams7.safewaterfall.swmanager.model.SirenEntity;

/**
 * @author CAMs7
 *
 */
@Repository
public interface SirenRepository extends JpaRepository<SirenEntity, Long> {

  // @EntityGraph(value = WITH_SENSORS)
  // Optional<SirenEntity> findById(Long id);

  @Query("SELECT sir FROM SirenEntity sir INNER JOIN sir.sensors sen WHERE sen.deviceId = ?1")
  Optional<SirenEntity> findBySensorDeviceId(String deviceId);
}
