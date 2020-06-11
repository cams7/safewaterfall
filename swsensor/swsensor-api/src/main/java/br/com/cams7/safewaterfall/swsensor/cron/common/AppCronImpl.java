package br.com.cams7.safewaterfall.swsensor.cron.common;

import java.text.ParseException;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import br.com.cams7.safewaterfall.common.error.AppException;
import br.com.cams7.safewaterfall.swsensor.service.common.AppSchedulerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AppCronImpl implements AppCron {

  @Autowired
  private AppSchedulerService schedulerService;

  protected CronTriggerFactoryBean cronTriggerFactoryBean;
  protected JobDetailFactoryBean jobDetailFactoryBean;

  public void register() {
    schedulerService.register(jobDetailFactoryBean.getObject(), cronTriggerFactoryBean.getObject());
    log.info("Register trigger {} with schedule {}", cronTriggerFactoryBean.getObject().getKey(),
        cronTriggerFactoryBean.getObject().getCronExpression());
  }

  public void reschedule(String cronExpression) {
    try {
      CronTriggerImpl cronTriggerImpl = (CronTriggerImpl) (cronTriggerFactoryBean.getObject());
      cronTriggerImpl.setCronExpression(cronExpression);
      schedulerService.reschedule(cronTriggerImpl);

      log.info("Reschedule trigger {} with new schedule {}", cronTriggerImpl.getKey(), cronTriggerImpl
          .getCronExpression());
    } catch (ParseException | RuntimeException e) {
      final String message = String.format(
          "Quartz Scheduler can not reschedule with the expression %s. the error is %s", cronExpression, e
              .getMessage());
      log.error(message);
      throw new AppException(message, e);
    }
  }

  public void pause() {
    try {
      CronTriggerImpl cronTriggerImpl = (CronTriggerImpl) (cronTriggerFactoryBean.getObject());
      schedulerService.pause(cronTriggerImpl);

      log.info("Pause trigger {} with new schedule {}", cronTriggerImpl.getKey(), cronTriggerImpl
          .getCronExpression());
    } catch (RuntimeException e) {
      final String message = String.format("Quartz Scheduler can not Pause the trigger, the error is %s", e
          .getMessage());
      log.error(message);
      throw new AppException(message, e);
    }
  }

  public void resume() {
    try {
      CronTriggerImpl cronTriggerImpl = (CronTriggerImpl) (cronTriggerFactoryBean.getObject());
      schedulerService.resume(cronTriggerImpl);

      log.info("Pause trigger {} with new schedule {}", cronTriggerImpl.getKey(), cronTriggerImpl
          .getCronExpression());
    } catch (RuntimeException e) {
      final String message = String.format("Quartz Scheduler can not Pause the trigger, the error is %s", e
          .getMessage());
      log.error(message);
      throw new AppException(message, e);
    }
  }

}
