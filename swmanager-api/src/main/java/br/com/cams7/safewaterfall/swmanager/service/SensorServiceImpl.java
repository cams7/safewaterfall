package br.com.cams7.safewaterfall.swmanager.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.cams7.safewaterfall.common.model.repository.SensorRepository;
import br.com.cams7.safewaterfall.common.model.vo.SensorVO;

@Service
public class SensorServiceImpl implements SensorService {

  private Logger LOG = Logger.getLogger(this.getClass().getName());

  @Autowired
  protected SensorRepository repository;

  /**
   *
   */
  @Override
  public void atualizarSensor(SensorVO sensor) {
    repository.save(sensor);
    LOG.log(Level.INFO, sensor.toString());
  }
}
