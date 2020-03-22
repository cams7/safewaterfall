/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.com.cams7.safewaterfall.swmanager.service.SirenService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author CAMs7
 *
 */
@Slf4j
@Component
public class SirenJob implements Job {

  @Autowired
  private SirenService sirenService;

  public void execute(JobExecutionContext context) throws JobExecutionException {
    log.info("SirenJob ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

    log.info("Next SirenJob scheduled @ {}", context.getNextFireTime());
  }
}
