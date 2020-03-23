/**
 * 
 */
package br.com.cams7.safewaterfall.arduino.model.vo;

import static br.com.cams7.safewaterfall.arduino.model.vo.Arduino.ArduinoTransmitter.OTHER_DEVICE;
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
@EqualsAndHashCode(of = {"transmitter", "status", "event"}, callSuper = true)
public abstract class Arduino extends ArduinoPin {

  public final static short PIN_VALUE_MIN = 0x0000;

  private ArduinoTransmitter transmitter;
  private ArduinoStatus status;
  private ArduinoEvent event;

  public Arduino() {
    super();
    setTransmitter(OTHER_DEVICE);
  }

  public Arduino(ArduinoStatus status, ArduinoEvent event, ArduinoPinType pinType, byte pin) {
    super(pinType, pin);

    setTransmitter(OTHER_DEVICE);
    setStatus(status);
    setEvent(event);

  }

  public void changeCurrentValues(Arduino arduino) {
    super.changeCurrentValues(arduino);

    setTransmitter(arduino.getTransmitter());
    setStatus(arduino.getStatus());
    setEvent(arduino.getEvent());
  }

  public enum ArduinoTransmitter {
    ARDUINO, // Mensagem enviada do Arduino
    OTHER_DEVICE; // Mensagem enviada do PC
  }

  public enum ArduinoStatus {
    SEND, // Mensagem de envio que nao exige uma resposta
    SEND_RESPONSE, // Mensagem de envio que exige uma resposta
    RESPONSE, // Mensagem de resposta
    RESPONSE_RESPONSE;// Mensagem de resposta que exige outra resposta
  }

  public enum ArduinoEvent {
    EXECUTE('x'), // Executa uma ação, ex: ACENDE ou APAGA um LED
    WRITE('w'), // Escreve o TIME e EVENTO para um determinado PINO no
                // Arduino
    READ('r'), // Lê o TIME e EVENTO para um determinado PINO no Arduino
    MESSAGE('m');// Caso aconteca algo fora do previsto o Arduino manda uma
    // mensagem

    private char abbreviation;

    private ArduinoEvent(char abbreviation) {
      this.abbreviation = abbreviation;
    }

    public char getAbbreviation() {
      return abbreviation;
    }
  }

}
