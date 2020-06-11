package br.com.cams7.safewaterfall.swsiren.service;

import org.springframework.stereotype.Service;
import br.com.cams7.safewaterfall.arduino.ArduinoServiceImpl;
import br.com.cams7.safewaterfall.arduino.model.vo.Arduino.ArduinoStatus;
import br.com.cams7.safewaterfall.arduino.model.vo.ArduinoPin.ArduinoPinType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StatusArduinoServiceImpl extends ArduinoServiceImpl implements StatusArduinoService {

  public final static byte DIGITAL_PIN = 12;

  public StatusArduinoServiceImpl() {
    super();
  }

  protected void receiveExecute(ArduinoPinType pinType, byte pin, short pinValue) {
    log.info("receiveExecute -> pinType: {}, pin: {}, pinValue: {}", pinType, pin, pinValue);

  }

  protected void receiveMessage(ArduinoPinType pinType, byte pin, short pinValue) {
    log.info("receiveMessage -> pinType: {}, pin: {}, pinValue: {}", pinType, pin, pinValue);
  }

  protected void receiveWrite(ArduinoPinType pinType, byte pin, byte threadInterval, byte actionEvent) {
    log.info("receiveWrite -> pinType: {}, pin: {}, threadInterval: {}, actionEvent: {}", pinType, pin,
        threadInterval, actionEvent);
  }

  protected void receiveRead(ArduinoPinType pinType, byte pin, byte threadInterval, byte actionEvent) {
    log.info("receiveRead -> pinType: {}, pin: {}, threadInterval: {}, actionEvent: {}", pinType, pin,
        threadInterval, actionEvent);
  }

  protected short sendResponse(ArduinoPinType pinType, byte pin, short pinValue) {
    log.info("sendResponse -> pinType: {}, pin: {}, pinValue: {}", pinType, pin, pinValue);
    return 0;
  }

  /**
   *
   */
  @Override
  public void changeSirenStatus(boolean active) {
    sendPinDigitalUSART(ArduinoStatus.SEND_RESPONSE, DIGITAL_PIN, active);
  }

}
