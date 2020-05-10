/**
 * 
 */
package br.com.cams7.safewaterfall.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.cams7.safewaterfall.common.error.AppResourceNotFoundException;
import br.com.cams7.safewaterfall.common.model.repository.AppSensorRepository;
import br.com.cams7.safewaterfall.common.model.vo.AppSensorVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author CAMs7
 *
 */
@Slf4j
@Service
public class AppSensorServiceImpl implements AppSensorService {

  @Autowired
  private AppSensorRepository repository;

  @Override
  public AppSensorVO save(AppSensorVO sensor) {
    log.info("save -> {}", sensor);
    return repository.save(sensor);
  }

  @Override
  public AppSensorVO findById(String id) {
    AppSensorVO sensor = repository.findById(id).orElseThrow(() -> new AppResourceNotFoundException(String.format(
        "Não foi encontrado nenhum sensor pelo id %d", id)));
    return sensor;
  }

  @Override
  public boolean existsById(String id) {
    return repository.existsById(id);
  }

}
