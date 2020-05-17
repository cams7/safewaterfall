package br.com.cams7.safewaterfall.swsensor.service;

import static br.com.cams7.safewaterfall.arduino.model.vo.Arduino.ArduinoEvent.MESSAGE;
import static br.com.cams7.safewaterfall.arduino.model.vo.ArduinoPin.ArduinoPinType.DIGITAL;
import static br.com.cams7.safewaterfall.swsensor.scheduler.AppQuartzConfig.SEND_MESSAGE_TRIGGER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import br.com.cams7.safewaterfall.arduino.ArduinoServiceImpl;
import br.com.cams7.safewaterfall.arduino.model.CurrentStatus;
import br.com.cams7.safewaterfall.arduino.model.vo.Arduino;
import br.com.cams7.safewaterfall.arduino.model.vo.Arduino.ArduinoEvent;
import br.com.cams7.safewaterfall.arduino.model.vo.Arduino.ArduinoStatus;
import br.com.cams7.safewaterfall.arduino.model.vo.ArduinoPin.ArduinoPinType;
import br.com.cams7.safewaterfall.arduino.model.vo.ArduinoUSART;
import br.com.cams7.safewaterfall.common.error.AppException;
import br.com.cams7.safewaterfall.common.error.AppResourceNotFoundException;
import br.com.cams7.safewaterfall.common.model.vo.AppSensorVO;
import br.com.cams7.safewaterfall.common.service.AppSchedulerService;
import br.com.cams7.safewaterfall.common.service.AppSensorService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StatusArduinoServiceImpl extends ArduinoServiceImpl implements StatusArduinoService {

  public final static byte DIGITAL_PIN = 8;

  @Value("${SENSOR_ID}")
  private String sensorId;

  @Autowired
  private AppSchedulerService appSchedulerService;

  @Autowired
  private AppSensorService appSensorService;

  public StatusArduinoServiceImpl() {
    super();
  }

  protected void receiveExecute(ArduinoPinType pinType, byte pin, short pinValue) {
    log.info("receiveExecute -> pinType: {}, pin: {}, pinValue: {}", pinType, pin, pinValue);
  }

  protected void receiveMessage(ArduinoPinType pinType, byte pin, short pinValue) {
    log.info("receiveMessage -> pinType: {}, pin: {}, pinValue: {}", pinType, pin, pinValue);
    AppSensorVO sensor = appSensorService.findById(sensorId);
    Short minimumAllowedDistance = sensor.getMinimumAllowedDistance();
    if (pinValue < minimumAllowedDistance) {
      String sendAlertMessageCron = sensor.getSendAlertMessageCron();
      appSchedulerService.reschedule(SEND_MESSAGE_TRIGGER, sendAlertMessageCron);
    } else {
      String sendStatusMessageCron = sensor.getSendStatusMessageCron();
      appSchedulerService.reschedule(SEND_MESSAGE_TRIGGER, sendStatusMessageCron);
    }
    log.info("Sensor -> {}", sensor);
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
   * Carrega a distancia lida pelo sensor ultrassonico
   */
  @Override
  public void loadDistance() {
    sendPinDigitalUSARTMessage(ArduinoStatus.SEND_RESPONSE, DIGITAL_PIN);
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

  /**
   *
   */
  @Override
  public short getDistance() {
    Arduino arduino = getArduinoResponse(DIGITAL, DIGITAL_PIN, MESSAGE);

    short distance = ((ArduinoUSART) arduino).getPinValue();
    return distance;
  }

}
