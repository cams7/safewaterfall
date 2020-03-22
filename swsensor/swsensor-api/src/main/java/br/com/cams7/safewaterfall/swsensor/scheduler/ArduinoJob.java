/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.com.cams7.safewaterfall.arduino.model.vo.ArduinoPin.ArduinoPinType;
import br.com.cams7.safewaterfall.common.model.PinPK;
import br.com.cams7.safewaterfall.swsensor.service.AppArduinoService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author CAMs7
 *
 */
@Slf4j
@Component
public class ArduinoJob implements Job {

  @Autowired
  private AppArduinoService arduinoService;

  public void execute(JobExecutionContext context) throws JobExecutionException {
    log.info("ArduinoJob ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

    PinPK pino = new PinPK(ArduinoPinType.DIGITAL, (short) 8);
    arduinoService.carregarDistancia(pino);

    log.info("Next ArduinoJob scheduled @ {}", context.getNextFireTime());
  }
}
