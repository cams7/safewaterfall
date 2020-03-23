/**
 * 
 */
package br.com.cams7.safewaterfall.common.scheduler;

import java.util.Calendar;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * @author CAMs7
 *
 */
public class AppQuartzUtil {

  public static SimpleTriggerFactoryBean createTrigger(JobDetail jobDetail, long pollFrequencyMs,
      String triggerName) {
    SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
    factoryBean.setJobDetail(jobDetail);
    factoryBean.setStartDelay(0L);
    factoryBean.setRepeatInterval(pollFrequencyMs);
    factoryBean.setName(triggerName);
    factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
    return factoryBean;
  }

  public static CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, String cronExpression,
      String triggerName) {
    // To fix an issue with time-based cron jobs
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
    factoryBean.setJobDetail(jobDetail);
    factoryBean.setCronExpression(cronExpression);
    factoryBean.setStartTime(calendar.getTime());
    factoryBean.setStartDelay(0L);
    factoryBean.setName(triggerName);
    factoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
    return factoryBean;
  }

  public static JobDetailFactoryBean createJobDetail(Class<? extends Job> jobClass, String jobName) {
    JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
    factoryBean.setName(jobName);
    factoryBean.setJobClass(jobClass);
    factoryBean.setDurability(true);
    return factoryBean;
  }
}
