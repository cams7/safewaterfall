/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.com.cams7.safewaterfall.arduino.model.vo.ArduinoPin.ArduinoPinType;
import br.com.cams7.safewaterfall.common.model.PinPK;
import br.com.cams7.safewaterfall.swsensor.service.AppArduinoService;

/**
 * @author CAMs7
 *
 */
@Component
public class ArduinoJob implements Job {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Autowired
  private AppArduinoService arduinoService;

  public void execute(JobExecutionContext context) throws JobExecutionException {
    LOGGER.info("Job ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

    PinPK pino = new PinPK(ArduinoPinType.DIGITAL, (short) 8);
    arduinoService.carregarDistancia(pino);

    LOGGER.info("Next job scheduled @ {}", context.getNextFireTime());
  }
}
