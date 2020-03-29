/**
 * 
 */
package br.com.cams7.safewaterfall.common.model.vo;

import org.springframework.data.redis.core.RedisHash;
import io.swagger.annotations.ApiModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author CAMs7
 *
 */
@ApiModel(description = "VO que representa o Sensor.")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id", callSuper = false)
@RedisHash("sensor")
public class AppSensorVO {

  private Long id;
  private Short distance;
  private String statusArduinoCron;
  private String sendStatusMessageCron;
  private String sendAlertMessageCron;
  private Short minimumAllowedDistance;
  private Short maximumMeasuredDistance;

  public AppSensorVO(Long id) {
    this();
    this.id = id;
  }

}
