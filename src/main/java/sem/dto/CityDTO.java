package sem.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CityDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	@NotNull(message = "Debe ingresar una hora de inicio")
	@NotBlank(message = "La hora de inicio no puede ser vacio")
	private String startTime;
	@NotNull(message = "Debe ingresar una hora fin")
	@NotBlank(message = "La hora de fin no puede ser vacio")
	private String endTime;
	@NotNull(message = "Debe ingresar el valor de estacionamiento a cobrar por hora")
	private double balancePerHs;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public double getBalancePerHs() {
		return balancePerHs;
	}

	public void setBalancePerHs(double balancePerHs) {
		this.balancePerHs = balancePerHs;
	}

}
