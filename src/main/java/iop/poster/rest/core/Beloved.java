package iop.poster.rest.core;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="beloved")
public class Beloved extends BaseEntity {

	public Beloved() {
		super();
	}

	public Beloved(String beloved) {
		super();
		this.beloved = beloved;
	}

	public Beloved(Long id, String beloved) {
		super(id);
		this.beloved = beloved;
	}
	
	@Basic
	private String beloved;

	public String getBeloved() {
		return beloved;
	}

	public void setBeloved(String beloved) {
		this.beloved = beloved;
	}

}
