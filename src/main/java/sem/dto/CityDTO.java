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
	@NotNull(message = "{startTime.notNull}")
	@NotBlank(message = "{startTime.notBlank}")
	private String startTime;
	@NotNull(message = "{endTime.notNull}")
	@NotBlank(message = "{endTime.notBlank}")
	private String endTime;
	@NotNull(message = "{value.notNull}")
	private double valueByHour;

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

	public double getValueByHour() {
		return valueByHour;
	}

	public void setValueByHour(double valueByHour) {
		this.valueByHour = valueByHour;
	}


}
