/**
 * 
 */
package br.com.cams7.safewaterfall.arduino;

/**
 * @author cesar
 *
 */
@SuppressWarnings("serial")
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
