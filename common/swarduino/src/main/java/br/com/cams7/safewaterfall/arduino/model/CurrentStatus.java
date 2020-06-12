/**
 * 
 */
package br.com.cams7.safewaterfall.arduino.model;

import java.io.Serializable;
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
public class CurrentStatus implements Serializable {

  private static final long serialVersionUID = 5803890142104358833L;

  private String id;
  private Arduino arduino;

  public CurrentStatus(String id) {
    this();
    this.id = id;
  }
}
