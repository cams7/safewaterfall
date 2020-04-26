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
@ApiModel(description = "VO que representa a Sirenas.")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id", callSuper = false)
@RedisHash("siren")
public class AppSirenVO {

  private Long id;
  private boolean active;

  public AppSirenVO(Long id) {
    this();
    this.id = id;
  }
}
