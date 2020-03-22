/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.com.cams7.safewaterfall.swmanager.service.SirenService;

/**
 * @author CAMs7
 *
 */
@Component
public class SirenJob implements Job {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Autowired
  private SirenService sirenService;

  public void execute(JobExecutionContext context) throws JobExecutionException {
    LOGGER.info("SirenJob ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

    LOGGER.info("Next SirenJob scheduled @ {}", context.getNextFireTime());
  }
}
