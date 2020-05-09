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

  @ApiModelProperty(notes = "Endereço da sirene", example = "http://127.0.0.1:80", required = true, position = 2)
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
   * @param appSiren VO da sirene
   * @return
   */
  public static SirenEntity getSiren(AppSirenVO appSiren) {
    SirenEntity siren = new SirenEntity(appSiren.getId());
    return siren;
  }

  /**
   * @param siren Entidade sirene
   * @return
   */
  public static AppSirenVO getSiren(SirenEntity siren) {
    AppSirenVO appSiren = new AppSirenVO(siren.getId());
    return appSiren;
  }

}
