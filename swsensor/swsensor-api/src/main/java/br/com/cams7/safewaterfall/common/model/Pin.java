package br.com.cams7.safewaterfall.common.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import br.com.cams7.safewaterfall.arduino.model.vo.ArduinoPin.ArduinoPinType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id", callSuper = false)
@MappedSuperclass
public abstract class Pin extends BaseEntity<PinPK> {

  @EmbeddedId
  private PinPK id;

  @NotNull
  @Enumerated(EnumType.ORDINAL)
  @Column(name = "evento")
  private Evento evento;

  @Column(name = "altera_evento", nullable = false)
  private boolean alteraEvento;

  @NotNull
  @Enumerated(EnumType.ORDINAL)
  @Column(name = "evento_intervalo")
  private Intervalo intervalo;

  @Column(name = "altera_intervalo", nullable = false)
  private boolean alteraIntervalo;

  public Pin(ArduinoPinType pinType, Short pin) {
    super(new PinPK(pinType, pin));
  }

  public enum Evento {
    ACENDE_APAGA, // Acende ou apaga
    PISCA_PISCA, // Pisca-pisca
    FADE, // Acende ao poucos
    NENHUM; // Não faz nada
  }

  public enum Intervalo {
    INTERVALO_10MILISEGUNDOS, // 1/100 de segundo
    INTERVALO_50MILISEGUNDOS, // 1/20 de segundo
    INTERVALO_100MILISEGUNDOS, // 1/10 de segundo
    INTERVALO_1SEGUNDO, // 1 segundo
    INTERVALO_3SEGUNDOS, // 3 segundos
    INTERVALO_5SEGUNDOS, // 5 segundos
    INTERVALO_10SEGUNDOS, // 10 segundos
    SEM_INTERVALO; // O evento sera apenas executado quando for chamado
  }

}
