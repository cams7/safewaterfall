/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.scheduler;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.com.cams7.safewaterfall.swsensor.service.StatusArduinoService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author CAMs7
 *
 */
@Slf4j
@Component
@DisallowConcurrentExecution
public class StatusArduinoJob implements Job {

  @Autowired
  private StatusArduinoService statusArduinoService;

  public void execute(JobExecutionContext context) throws JobExecutionException {
    log.info("StatusArduinoJob ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context
        .getFireTime());

    statusArduinoService.loadDistance();

    log.info("Next StatusArduinoJob scheduled @ {}", context.getNextFireTime());
  }
}
