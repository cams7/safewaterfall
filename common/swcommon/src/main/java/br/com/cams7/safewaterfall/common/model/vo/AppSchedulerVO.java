/**
 * 
 */
package br.com.cams7.safewaterfall.common.model.vo;

import org.springframework.data.redis.core.RedisHash;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author CAMs7
 *
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id", callSuper = false)
@RedisHash("scheduler")
public class AppSchedulerVO {
  private String id; // Trigger name
  private String cronExpression;

  public AppSchedulerVO(String id) {
    this();
    this.id = id;
  }
}
