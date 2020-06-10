package br.com.cams7.safewaterfall.common.cron;

import java.text.ParseException;
import java.util.logging.Level;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import br.com.cams7.safewaterfall.common.error.AppException;
import br.com.cams7.safewaterfall.common.service.SchedulerService;
import lombok.extern.java.Log;

@Log
public abstract class AppCronImpl implements AppCron {

  @Autowired
  private SchedulerService schedulerService;

  protected CronTriggerFactoryBean cronTriggerFactoryBean;
  protected JobDetailFactoryBean jobDetailFactoryBean;

  public void register() {
    schedulerService.register(jobDetailFactoryBean.getObject(), cronTriggerFactoryBean.getObject());
    log.log(Level.INFO, "Register trigger {0} with schedule {1}", new Object[] {cronTriggerFactoryBean.getObject()
        .getKey(), cronTriggerFactoryBean.getObject().getCronExpression()});
  }

  public void reschedule(String cronExpression) {
    try {
      CronTriggerImpl cronTriggerImpl = (CronTriggerImpl) (cronTriggerFactoryBean.getObject());
      cronTriggerImpl.setCronExpression(cronExpression);
      schedulerService.reschedule(cronTriggerImpl);

      log.log(Level.INFO, "Reschedule trigger {0} with new schedule {1}", new Object[] {cronTriggerImpl.getKey(),
          cronTriggerImpl.getCronExpression()});
    } catch (ParseException | RuntimeException e) {
      final String message = String.format(
          "Quartz Scheduler can not reschedule with the expression %s. the error is %s", cronExpression, e
              .getMessage());
      log.log(Level.SEVERE, message);
      throw new AppException(message, e);
    }
  }

  public void pause() {
    try {
      CronTriggerImpl cronTriggerImpl = (CronTriggerImpl) (cronTriggerFactoryBean.getObject());
      schedulerService.pause(cronTriggerImpl);

      log.log(Level.INFO, "Pause trigger {0} with new schedule {1}", new Object[] {cronTriggerImpl.getKey(),
          cronTriggerImpl.getCronExpression()});
    } catch (RuntimeException e) {
      final String message = String.format("Quartz Scheduler can not Pause the trigger, the error is %s", e
          .getMessage());
      log.log(Level.SEVERE, message);
      throw new AppException(message, e);
    }
  }

  public void resume() {
    try {
      CronTriggerImpl cronTriggerImpl = (CronTriggerImpl) (cronTriggerFactoryBean.getObject());
      schedulerService.resume(cronTriggerImpl);

      log.log(Level.INFO, "Pause trigger {0} with new schedule {1}", new Object[] {cronTriggerImpl.getKey(),
          cronTriggerImpl.getCronExpression()});
    } catch (RuntimeException e) {
      final String message = String.format("Quartz Scheduler can not Pause the trigger, the error is %s", e
          .getMessage());
      log.log(Level.SEVERE, message);
      throw new AppException(message, e);
    }
  }

}
