package br.com.cams7.safewaterfall.swmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.cams7.safewaterfall.common.model.repository.AppSensorRepository;
import br.com.cams7.safewaterfall.common.model.vo.AppSensorVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SensorServiceImpl implements SensorService {

  @Autowired
  protected AppSensorRepository repository;

  /**
   *
   */
  @Override
  public void atualizarSensor(AppSensorVO sensor) {
    repository.save(sensor);
    log.info("atualizarSensor -> {}", sensor);
  }
}
