package sem.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "city")
public class City {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "startTime")
	private String startTime;
	@Column(name = "endTime")
	private String endTime;
	@Column(name = "valueByHour")
	private double valueByHour;

	public City() {
	}

	public City(String startTime, String endTime, double valueByHour) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.valueByHour = valueByHour;
	}

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
