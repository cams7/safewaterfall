/**
 * 
 */
package br.com.cams7.safewaterfall.common.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author cams7
 *
 * @param <PK>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseEntity<PK extends Serializable> {

  public static final String UUID_V4_REGEX =
      "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

  public BaseEntity() {
    super();
  }

  /**
   * @param id
   */
  public BaseEntity(PK id) {
    this();

    setId(id);
  }

  public abstract PK getId();

  public abstract void setId(PK id);

}
