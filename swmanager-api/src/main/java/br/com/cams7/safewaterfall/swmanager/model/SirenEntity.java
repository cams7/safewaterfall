/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.model;

import static javax.persistence.GenerationType.SEQUENCE;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import br.com.cams7.safewaterfall.common.model.BaseEntity;
import br.com.cams7.safewaterfall.common.model.vo.AppSirenVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author CAMs7
 *
 */
@ApiModel(description = "Entidade que representa o sirene")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "TB_SIRENE")
public class SirenEntity extends BaseEntity<Long> {

  @ApiModelProperty(notes = "Identificador único do sirene", example = "1", required = true, position = 1)
  @Id
  @SequenceGenerator(name = "SQ_SIRENE", sequenceName = "SQ_SIRENE", allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "SQ_SIRENE")
  @Column(name = "ID_SIRENE", nullable = false, updatable = false)
  private Long id;

  @ApiModelProperty(notes = "Identificador único do dispositivo relacionado", example = "UUID V4", required = true,
      position = 2)
  @NotBlank
  @Pattern(regexp = UUID_V4_REGEX)
  @Column(name = "ID_DISPOSITIVO", nullable = false, updatable = false)
  private String deviceId;

  @ApiModelProperty(notes = "Endereço da sirene", example = "http://127.0.0.1:80", required = true, position = 3)
  @NotBlank
  @Size(min = 19, max = 100)
  @Column(name = "ENDERECO_SIRENE", nullable = false)
  private String sirenAddress;

  @OneToMany(mappedBy = "siren")
  private List<SensorEntity> sensors;

  public SirenEntity(Long id) {
    this();
    this.id = id;
  }

  /**
   * @param siren Entidade sirene
   * @param appSiren VO da sirene
   */
  public static void setSiren(SirenEntity siren, AppSirenVO appSiren) {
    siren.setDeviceId(appSiren.getId());
  }

  /**
   * @param appSiren VO da sirene
   * @return Entidade sirene
   */
  public static SirenEntity getSiren(AppSirenVO appSiren) {
    SirenEntity siren = new SirenEntity();
    setSiren(siren, appSiren);
    return siren;
  }

  /**
   * @param appSiren VO da sirene
   * @param siren Entidade sirene
   */
  public static void setSiren(AppSirenVO appSiren, SirenEntity siren) {
    appSiren.setId(siren.getDeviceId());
  }

  /**
   * @param siren Entidade sirene
   * @return VO da sirene
   */
  public static AppSirenVO getSiren(SirenEntity siren) {
    AppSirenVO appSiren = new AppSirenVO();
    setSiren(appSiren, siren);
    return appSiren;
  }

}
