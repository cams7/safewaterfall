package br.com.cams7.safewaterfall.swmanager.model.vo;

import static br.com.cams7.safewaterfall.common.model.BaseEntity.UUID_V4_REGEX;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.data.redis.core.RedisHash;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(description = "VO que representa a sirene.")
@NoArgsConstructor
@Data(staticConstructor = "of")
@EqualsAndHashCode(of = "id", callSuper = false)
@RedisHash("siren")
public class AppSirenVO implements Serializable {

  private static final long serialVersionUID = -402873148589542654L;

  @ApiModelProperty(notes = "Identificador único da sirene", example = "UUID V4", required = true, position = 1)
  @NotBlank
  @Pattern(regexp = UUID_V4_REGEX)
  private String id;

  @ApiModelProperty(notes = "Endereço da sirene", example = "http://127.0.0.1:80", required = true, position = 2)
  @NotBlank
  @Size(min = 19, max = 100)
  private String address;

  @ApiModelProperty(notes = "Sirene esta ativa", example = "true", required = true, position = 3)
  private boolean active;

  public AppSirenVO(@NotBlank @Pattern(regexp = UUID_V4_REGEX) String id) {
    this();
    this.id = id;
  }

}
