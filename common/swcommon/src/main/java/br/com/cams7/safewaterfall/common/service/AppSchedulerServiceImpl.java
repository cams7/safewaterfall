/**
 * 
 */
package br.com.cams7.safewaterfall.common.service;

import java.text.ParseException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import br.com.cams7.safewaterfall.common.error.AppException;
import br.com.cams7.safewaterfall.common.model.repository.AppSchedulerRepository;
import br.com.cams7.safewaterfall.common.model.vo.AppSchedulerVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author CAMs7
 *
 */
@Slf4j
@Service
public class AppSchedulerServiceImpl implements AppSchedulerService {

  @Autowired
  private SchedulerFactoryBean schedulerFactory;

  @Autowired
  private AppSchedulerRepository schedulerRepository;

  /**
   * @param triggerName Nome do trigger
   * @param cronExpression Expressão Cron
   */
  private void rescheduleCronJob(String triggerName, String cronExpression) {
    Scheduler scheduler = schedulerFactory.getScheduler();
    TriggerKey triggerKey = new TriggerKey(triggerName);

    try {
      CronTriggerImpl trigger = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
      trigger.setCronExpression(cronExpression);
      scheduler.rescheduleJob(triggerKey, trigger);
      log.info("O cron do trigger {} foi alterado para {}", triggerName, cronExpression);
    } catch (SchedulerException | ParseException e) {
      throw new AppException(e);
    }
  }

  /**
   * @param triggerName Nome do trigger
   * @param cronExpression Expressão Cron
   */
  public void reschedule(String triggerName, String cronExpression) {
    boolean reschedule = !schedulerRepository.existsById(triggerName);
    if (!reschedule) {
      AppSchedulerVO scheduler = schedulerRepository.findById(triggerName).orElseThrow(() -> new AppException(
          String.format("O trigger %s não foi encontrado", triggerName)));
      reschedule = !cronExpression.equals(scheduler.getCronExpression());
    }

    if (reschedule) {
      AppSchedulerVO scheduler = new AppSchedulerVO(triggerName);
      scheduler.setCronExpression(cronExpression);
      rescheduleCronJob(triggerName, cronExpression);
      schedulerRepository.save(scheduler);
    }
  }

}
