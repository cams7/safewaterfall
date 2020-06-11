package br.com.cams7.safewaterfall.common.service;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;

public interface AppSchedulerService {
  void register(JobDetail jobDetail, CronTrigger cronTrigger);

  void reschedule(CronTrigger cronTrigger);

  void pause(CronTrigger cronTrigger);

  void resume(CronTrigger cronTrigger);
}
