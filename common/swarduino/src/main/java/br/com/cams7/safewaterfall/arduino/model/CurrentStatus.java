/**
 * 
 */
package br.com.cams7.safewaterfall.arduino.model;

import org.springframework.data.redis.core.RedisHash;
import br.com.cams7.safewaterfall.arduino.model.vo.Arduino;
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
@RedisHash("current_status")
public class CurrentStatus {
  private String id;
  private Arduino arduino;

  public CurrentStatus(String id) {
    this();
    this.id = id;
  }
}
