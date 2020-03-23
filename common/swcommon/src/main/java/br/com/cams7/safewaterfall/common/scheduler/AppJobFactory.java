/**
 * 
 */
package br.com.cams7.safewaterfall.common.scheduler;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * @author CAMs7
 *
 */
public final class AppJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

  private transient AutowireCapableBeanFactory beanFactory;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    beanFactory = applicationContext.getAutowireCapableBeanFactory();
  }

  @Override
  protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
    final Object job = super.createJobInstance(bundle);
    beanFactory.autowireBean(job);
    return job;
  }

}
