/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.arduino.model.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author cams7
 *
 */
@SuppressWarnings("serial")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"threadInterval", "actionEvent"}, callSuper = true)
public abstract class ArduinoEEPROM extends Arduino implements EEPROMData {

  private byte threadInterval;
  private byte actionEvent;

  /**
   * 
   */
  public ArduinoEEPROM() {
    super();

    setThreadInterval((byte) 0x00);
    setActionEvent((byte) 0x00);
  }

  /**
   * @param status
   * @param event
   * @param pinType
   * @param pin
   */
  public ArduinoEEPROM(ArduinoStatus status, ArduinoEvent event, ArduinoPinType pinType, byte pin, byte threadTime,
      byte actionEvent) {
    super(status, event, pinType, pin);

    setThreadInterval(threadTime);
    setActionEvent(actionEvent);
  }

  public void changeCurrentValues(ArduinoEEPROM arduino) {
    super.changeCurrentValues(arduino);

    setThreadInterval(arduino.getThreadInterval());
    setActionEvent(arduino.getActionEvent());
  }

}
