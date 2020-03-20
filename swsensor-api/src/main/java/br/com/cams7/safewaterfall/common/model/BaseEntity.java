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
