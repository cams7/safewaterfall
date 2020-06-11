package br.com.cams7.safewaterfall.swsensor.cron.common;

public interface AppCron {
  void register();

  void reschedule(String cronExpression);

  void pause();

  void resume();
}
