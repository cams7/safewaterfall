/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.model.vo;

import static br.com.cams7.safewaterfall.common.model.BaseEntity.UUID_V4_REGEX;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.springframework.data.redis.core.RedisHash;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author CAMs7
 *
 */
@ApiModel(description = "VO que representa o sensor.")
@NoArgsConstructor
@Data(staticConstructor = "of")
@EqualsAndHashCode(of = "id", callSuper = false)
@RedisHash("sensor")
public class AppSensorVO implements Serializable {

  private static final long serialVersionUID = -4824670937155319120L;

  @ApiModelProperty(notes = "Identificador único do sensor", example = "UUID V4", required = true, position = 1)
  @NotBlank
  @Pattern(regexp = UUID_V4_REGEX)
  private String id;

  @ApiModelProperty(notes = "Identificador único da sirene", example = "UUID V4", required = true, position = 2)
  @NotBlank
  @Pattern(regexp = UUID_V4_REGEX)
  private String sirenId;

  @ApiModelProperty(notes = "Sirene esta ativa", example = "true", required = true, position = 3)
  private boolean active;

  public AppSensorVO(@NotBlank @Pattern(regexp = UUID_V4_REGEX) String id) {
    this();
    this.id = id;
  }

  public AppSensorVO(@NotBlank @Pattern(regexp = UUID_V4_REGEX) String id, @NotBlank @Pattern(
      regexp = UUID_V4_REGEX) String sirenId) {
    this(id);
    this.sirenId = sirenId;
  }
}
