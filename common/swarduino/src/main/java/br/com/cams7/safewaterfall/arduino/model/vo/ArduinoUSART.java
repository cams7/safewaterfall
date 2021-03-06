/**
 * 
 */
package br.com.cams7.safewaterfall.arduino.model.vo;

import static br.com.cams7.safewaterfall.arduino.model.vo.Arduino.ArduinoEvent.EXECUTE;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author cams7
 *
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"pinValue"}, callSuper = true)
public class ArduinoUSART extends Arduino {

  // Valor maximo da PORTA DIGITAL e 255
  public static final short DIGITAL_PIN_VALUE_MAX = 0xFF;
  // Valor maximo da PORTA ANALOGICA e 1023
  public static final short ANALOG_PIN_VALUE_MAX = 0x3FF;

  private short pinValue;

  public ArduinoUSART() {
    super();

    setEvent(EXECUTE);
    setPinValue(PIN_VALUE_MIN);
  }

  protected ArduinoUSART(ArduinoStatus status, ArduinoEvent event, ArduinoPinType pinType, byte pin,
      short pinValue) {
    super(status, event, pinType, pin);

    setPinValue(pinValue);
  }

  public ArduinoUSART(ArduinoStatus status, ArduinoPinType pinType, byte pin, short pinValue) {
    this(status, EXECUTE, pinType, pin, pinValue);
  }

  public void changeCurrentValues(ArduinoUSART arduino) {
    super.changeCurrentValues(arduino);

    setPinValue(arduino.getPinValue());
  }

}
