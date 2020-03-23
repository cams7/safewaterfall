/**
 * 
 */
package br.com.cams7.safewaterfall.arduino.error;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author cesar
 *
 */
@SuppressWarnings("serial")
@ResponseStatus(value = BAD_GATEWAY)
public class ArduinoException extends RuntimeException {

  public ArduinoException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public ArduinoException(String msg) {
    super(msg);
  }

  public ArduinoException(Throwable cause) {
    super(cause);
  }

}
