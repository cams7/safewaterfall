/**
 * 
 */
package br.com.cams7.safewaterfall.common.service;

/**
 * @author CAMs7
 *
 */
public interface AppSchedulerService {
  void reschedule(String triggerName, String cronExpression);
}
