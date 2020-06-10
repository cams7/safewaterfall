package br.com.cams7.safewaterfall.common.cron;

public interface AppCron {
  void register();

  void reschedule(String cronExpression);

  void pause();

  void resume();
}
