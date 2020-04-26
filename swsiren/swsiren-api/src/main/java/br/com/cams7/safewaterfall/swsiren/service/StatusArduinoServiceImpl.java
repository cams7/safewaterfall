package br.com.cams7.safewaterfall.swsiren.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.cams7.safewaterfall.arduino.ArduinoServiceImpl;
import br.com.cams7.safewaterfall.arduino.model.CurrentStatus;
import br.com.cams7.safewaterfall.arduino.model.vo.Arduino;
import br.com.cams7.safewaterfall.arduino.model.vo.Arduino.ArduinoEvent;
import br.com.cams7.safewaterfall.arduino.model.vo.Arduino.ArduinoStatus;
import br.com.cams7.safewaterfall.arduino.model.vo.ArduinoPin.ArduinoPinType;
import br.com.cams7.safewaterfall.common.error.AppException;
import br.com.cams7.safewaterfall.common.error.AppResourceNotFoundException;
import br.com.cams7.safewaterfall.common.service.AppSchedulerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StatusArduinoServiceImpl extends ArduinoServiceImpl implements StatusArduinoService {

  public final static byte DIGITAL_PIN = 12;

  @Autowired
  private AppSchedulerService appSchedulerService;

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
   * @param pinType Tipo do pino
   * @param pin Numero do pino
   * @param event Evento
   * @return
   */
  private Arduino getArduinoResponse(ArduinoPinType pinType, byte pin, ArduinoEvent event) {
    String id = getKeyCurrentStatus(event, pinType, pin);

    if (!statusRepository.existsById(id))
      throw new AppResourceNotFoundException(String.format(
          "Não foi encontrado nenhum estado do arduino pelo id %s", id));

    CurrentStatus currentStatus = statusRepository.findById(id).orElseThrow(() -> new AppResourceNotFoundException(
        String.format("Não foi encontrado nenhum estado do arduino pelo id %s", id)));
    Arduino arduino = currentStatus.getArduino();

    if (arduino.getTransmitter() != Arduino.ArduinoTransmitter.ARDUINO)
      throw new AppException(String.format("O transmissor esperado era o %s, mas foi retornado %s",
          Arduino.ArduinoTransmitter.ARDUINO, arduino.getTransmitter()));

    if (arduino.getStatus() != ArduinoStatus.RESPONSE)
      throw new AppException(String.format("O status do arduino esperado era o %s, mas foi retornado %s",
          ArduinoStatus.RESPONSE, arduino.getStatus()));

    return arduino;
  }

  @Override
  public void alteraEstadoSirene(boolean isAtiva) {
    sendPinDigitalUSART(ArduinoStatus.SEND_RESPONSE, DIGITAL_PIN, isAtiva);
  }

}
