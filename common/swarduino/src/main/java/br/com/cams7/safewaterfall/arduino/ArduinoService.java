/**
 * 
 */
package br.com.cams7.safewaterfall.arduino;

/**
 * @author cams7
 *
 */
public interface ArduinoService {
  /**
   * Porta Serial
   * 
   * @return
   */
  String getSerialPort();

  /**
   * Baud rate
   * 
   * @return
   */
  int getSerialBaudRate();

  /**
   * Serial thread time
   * 
   * @return
   */
  long getSerialThreadTime();
}
